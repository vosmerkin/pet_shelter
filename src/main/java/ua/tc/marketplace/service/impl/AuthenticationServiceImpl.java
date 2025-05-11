package ua.tc.marketplace.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ua.tc.marketplace.config.UserDetailsImpl;
import ua.tc.marketplace.exception.auth.BadCredentialsAuthenticationException;
import ua.tc.marketplace.exception.auth.EmailAlreadyRegisteredException;
import ua.tc.marketplace.exception.auth.EmailVerificationTokenNotFoundOrExpiredException;
import ua.tc.marketplace.exception.auth.GeneralAuthenticationException;
import ua.tc.marketplace.exception.user.UserNotFoundException;
import ua.tc.marketplace.jwtAuth.JwtConfig;
import ua.tc.marketplace.jwtAuth.JwtUtil;
import ua.tc.marketplace.model.VerificationToken;
import ua.tc.marketplace.model.auth.AuthRequest;
import ua.tc.marketplace.model.auth.AuthResponse;
import ua.tc.marketplace.model.dto.user.CreateUserDto;
import ua.tc.marketplace.model.entity.User;
import ua.tc.marketplace.repository.UserRepository;
import ua.tc.marketplace.repository.VerificationTokenRepository;
import ua.tc.marketplace.service.AuthenticationService;
import ua.tc.marketplace.service.UserService;
import ua.tc.marketplace.service.VerificationTokenService;
import ua.tc.marketplace.util.MailService;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {


    @Value("${verification.token.expiryTimeInMinuets}")
    private int expiryTimeInMinutes;

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final JwtConfig jwtConfig;
    private final UserService userService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;
    private final MailService mailService;
    private final VerificationTokenService verificationTokenService;


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
            return new AuthResponse(user.getId(), email, token, "", jwtConfig.getTokenExpirationAfterSeconds());
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

    @Override
    public void registerUserWithVerify(CreateUserDto userDto) {
        if (userService.UserExistsByEmail(userDto.email()))
            throw new EmailAlreadyRegisteredException(userDto.email());
        userService.createUser(userDto);
        VerificationToken token =
                new VerificationToken(userService.findUserByEmail(userDto.email()),
                        expiryTimeInMinutes);

        verificationTokenRepository.save(token);

        mailService.sendVerificationEmail(userDto.email(), token.getToken());
    }

    @Override
    public Optional<User> getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (authentication != null
                && principal instanceof UserDetailsImpl) {
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
    public Boolean verifyEmail(String token) {
        //before verifying clear expired tokens
        verificationTokenService.clearExpiredTokens();

        VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);
        if (userService.UserExistsByEmail(verificationToken.getUser().getEmail()) ||
                verificationToken.getExpiryDate().after(Date.from(Instant.now()))) {
            throw new EmailVerificationTokenNotFoundOrExpiredException();
        }
        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        verificationTokenService.delete(verificationToken.getId());
        return true;
    }

    public boolean hasId(Long id) {
        User authenticatedUser = getAuthenticatedUser().orElseThrow(() -> new UserNotFoundException(id));
        return authenticatedUser.getId().equals(id);

    }
}
