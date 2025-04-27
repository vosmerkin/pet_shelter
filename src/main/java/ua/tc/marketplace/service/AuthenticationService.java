package ua.tc.marketplace.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import ua.tc.marketplace.model.auth.AuthRequest;
import ua.tc.marketplace.model.auth.AuthResponse;
import ua.tc.marketplace.model.dto.user.CreateUserDto;
import ua.tc.marketplace.model.dto.user.UserDto;
import ua.tc.marketplace.model.entity.User;

public interface AuthenticationService {

  AuthResponse authenticate(AuthRequest authRequest);

  AuthResponse registerUser(CreateUserDto userDto);

  void registerUser(String email);

  Optional<User> getAuthenticatedUser();

    UserDto verifyEmail(String token);
}
