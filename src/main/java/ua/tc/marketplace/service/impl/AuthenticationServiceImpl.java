package ua.tc.marketplace.service.impl;

import java.util.Objects;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ua.tc.marketplace.config.UserDetailsImpl;
import ua.tc.marketplace.exception.auth.BadCredentialsAuthenticationException;
import ua.tc.marketplace.exception.auth.GeneralAuthenticationException;
import ua.tc.marketplace.exception.user.UserNotFoundException;
import ua.tc.marketplace.jwtAuth.JwtConfig;
import ua.tc.marketplace.jwtAuth.JwtUtil;
import ua.tc.marketplace.model.auth.AuthRequest;
import ua.tc.marketplace.model.auth.AuthResponse;
import ua.tc.marketplace.model.dto.user.CreateUserDto;
import ua.tc.marketplace.model.entity.User;
import ua.tc.marketplace.service.AuthenticationService;
import ua.tc.marketplace.service.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final JwtConfig jwtConfig;
    private final UserService userService;

    /**
     * Authentificats a user/
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
            return new AuthResponse(user.getId(),email, token, "", jwtConfig.getTokenExpirationAfterSeconds());
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
    public Optional<User> getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && authentication.getPrincipal() instanceof UserDetailsImpl principal) {
            return Optional.of(principal.getUser());
        } else if (authentication instanceof UsernamePasswordAuthenticationToken) {
            String email = (String) authentication.getPrincipal();
            User user = userService.findUserByEmail(email);

            return Optional.of(user);
        }
        log.info(
                "Authentication is null or principal is not of type UserDetailsImpl or UsernamePasswordAuthenticationToken");
        return Optional.empty();
    }

    public boolean hasId(Long id) {
        User authenticatedUser = getAuthenticatedUser().orElseThrow(() -> new UserNotFoundException(id));
        return authenticatedUser.getId().equals(id);

    }
}
