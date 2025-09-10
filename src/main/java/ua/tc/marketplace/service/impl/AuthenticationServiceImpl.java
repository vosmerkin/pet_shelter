package ua.tc.marketplace.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ua.tc.marketplace.config.UserDetailsImpl;
import ua.tc.marketplace.exception.auth.BadCredentialsAuthenticationException;
import ua.tc.marketplace.exception.auth.VerificationTokenNotFoundOrExpiredException;
import ua.tc.marketplace.exception.auth.GeneralAuthenticationException;
import ua.tc.marketplace.exception.user.UserNotFoundException;
import ua.tc.marketplace.jwtAuth.JwtConfig;
import ua.tc.marketplace.jwtAuth.JwtUtil;
import ua.tc.marketplace.model.VerificationToken;
import ua.tc.marketplace.model.auth.AuthRequest;
import ua.tc.marketplace.model.auth.AuthResponse;
import ua.tc.marketplace.model.auth.PasswordChangeRequest;
import ua.tc.marketplace.model.dto.user.CreateUserDto;
import ua.tc.marketplace.model.dto.user.UpdateUserDto;
import ua.tc.marketplace.model.dto.user.UserDto;
import ua.tc.marketplace.model.entity.User;
import ua.tc.marketplace.repository.UserRepository;
import ua.tc.marketplace.repository.VerificationTokenRepository;
import ua.tc.marketplace.service.AuthenticationService;
import ua.tc.marketplace.service.UserService;
import ua.tc.marketplace.service.VerificationTokenService;
import ua.tc.marketplace.util.MailService;
import ua.tc.marketplace.util.OnForgetPasswordEvent;
import ua.tc.marketplace.util.OnRegistrationCompleteEvent;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;
  private final JwtConfig jwtConfig;
  private final UserService userService;
  private final VerificationTokenRepository verificationTokenRepository;
  private final UserRepository userRepository;
  private final VerificationTokenService verificationTokenService;
  private final ApplicationEventPublisher eventPublisher;
  private final PasswordEncoder passwordEncoder;
  private final UserDetailsImpl userDetails;

  /**
   * Authenticates a user/
   *
   * @param authRequest The email of the user to retrieve.
   * @return The UserDto representing the found user.
   * @throws UserNotFoundException If the user is not found.
   */
  @Override
  public AuthResponse authenticate(AuthRequest authRequest) {
    try {
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(authRequest.email(), authRequest.password()));
      String email = authentication.getName();
      User user = userService.findUserByEmail(email);
      String token = jwtUtil.createToken(user);
      return new AuthResponse(
          user.getId(), email, token, "", jwtConfig.getTokenExpirationAfterSeconds());
    } catch (BadCredentialsException e) {
      throw new BadCredentialsAuthenticationException();
    } catch (Exception e) {
      throw new GeneralAuthenticationException(e.getMessage());
    }
  }

  @Override
  public AuthResponse registerUser(CreateUserDto userDto) {
    userService.createUser(userDto);
    return authenticate(new AuthRequest(userDto.email(), userDto.password()));
  }

  @Transactional
  @Override
  public String registerUserWithVerify(CreateUserDto userDto) {
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    log.info("request.getContextPath() = {}", request.getContextPath());

    UserDto newUserDto = userService.createUser(userDto);
    VerificationToken token =
        new VerificationToken(
            userService.findUserByEmail(userDto.email()), VerificationToken.TokenType.REGISTRATION);

    verificationTokenRepository.save(token);

    eventPublisher.publishEvent(new OnRegistrationCompleteEvent(newUserDto, token.getToken()));
    //        mailService.sendVerificationEmailResend(userDto.email(), token.getToken());
    return token.getToken();
  }

  @Override
  public Optional<User> getAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Object principal = authentication.getPrincipal();
    if (authentication != null && principal instanceof UserDetailsImpl) {
      return Optional.of(((UserDetailsImpl) principal).getUser());
    } else if (authentication instanceof UsernamePasswordAuthenticationToken) {
      String email = (String) authentication.getPrincipal();
      User user = userService.findUserByEmail(email);

      return Optional.of(user);
    }
    log.info(
        "Authentication is null or principal is not of type UserDetailsImpl or UsernamePasswordAuthenticationToken");
    return Optional.empty();
  }

  @Override
  public boolean verifyEmail(String token) {
    VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);
    verificationTokenService.verifyToken(
        verificationToken, VerificationToken.TokenType.REGISTRATION);
    User user = verificationToken.getUser();
    user.setEnabled(true);
    userRepository.save(user);
    verificationTokenService.delete(verificationToken.getId());
    return true;
  }

  @Override
  public AuthResponse verifyEmailWithLogin(String token) {
    if (verifyEmail(token)) {
      try {
        VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);
        User user = verificationToken.getUser();
        Authentication authentication =
            new UsernamePasswordAuthenticationToken(user, null, userDetails.getAuthorities());
        //                        AuthorityUtils.createAuthorityList("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
//        Authentication authentication =
//            authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                    authRequest.email(), authRequest.password()));
//        String email = authentication.getName();
//        User user = userService.findUserByEmail(email);
        String authToken = jwtUtil.createToken(user);
        return new AuthResponse(
            user.getId(), user.getEmail(), authToken, "", jwtConfig.getTokenExpirationAfterSeconds());
      } catch (BadCredentialsException e) {
        throw new BadCredentialsAuthenticationException();
      } catch (Exception e) {
        throw new GeneralAuthenticationException(e.getMessage());
      }
    }

    return null;
  }

  @Override
  public void forgetPasswordRequest(String email) {
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    log.info("request.getContextPath() = {}", request.getContextPath());
    log.debug("request.getContextPath() = {}", request.getContextPath());
    // check email

    if (!userService.UserExistsByEmail(email)) throw new UserNotFoundException(email);
    // create token
    verificationTokenService.clearExpiredTokens();
    VerificationToken token =
        new VerificationToken(
            userService.findUserByEmail(email), VerificationToken.TokenType.PASSWORD_RESET);
    verificationTokenRepository.save(token);
    // create event for mail sending
    eventPublisher.publishEvent(new OnForgetPasswordEvent(token));
  }

  @Override
  public boolean verifyResetPasswordToken(String token) {
    log.debug("Verifying token {}", token);
    VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);
    return verificationTokenService.verifyToken(
        verificationToken, VerificationToken.TokenType.PASSWORD_RESET);
  }

  @Override
  public boolean resetPassword(PasswordChangeRequest request) {
    VerificationToken verificationToken =
        verificationTokenService.getVerificationToken(request.passwordResetToken());
    if (!verificationTokenService.verifyToken(
        verificationToken, VerificationToken.TokenType.PASSWORD_RESET))
      throw new VerificationTokenNotFoundOrExpiredException(verificationToken.getToken());
    User user = userService.findUserByEmail(request.email());
    UpdateUserDto updateUserDto =
        UpdateUserDto.builder()
            .id(user.getId())
            .password(passwordEncoder.encode(request.password()))
            .build();
    userService.updateUser(updateUserDto);
    return true;
  }

  public boolean hasId(Long id) {
    User authenticatedUser =
        getAuthenticatedUser().orElseThrow(() -> new UserNotFoundException(id));
    return authenticatedUser.getId().equals(id);
  }
}
