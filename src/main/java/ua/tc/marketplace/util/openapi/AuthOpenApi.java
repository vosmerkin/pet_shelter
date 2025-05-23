package ua.tc.marketplace.util.openapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.tc.marketplace.controller.UserController;
import ua.tc.marketplace.model.auth.AuthRequest;
import ua.tc.marketplace.model.auth.AuthResponse;
import ua.tc.marketplace.model.dto.user.CreateUserDto;

/**
 * This interface defines the OpenAPI annotations for the {@link UserController} class. It provides endpoints
 * for managing users.
 */
@Tag(name = "Auth API", description = "API for logging in and registering users")
public interface AuthOpenApi {

  @Operation(
          summary = "Authenticates a user",
          description = "Authenticates a user and creates jwt token.")
  @PostMapping("/login")
  ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest authRequest);

  @Operation(
      summary = "Creates a new user",
      description = "Creates a new user based on the provided data.")
  @PostMapping("/signup")
  ResponseEntity<AuthResponse> registerUser( @Valid @RequestBody CreateUserDto userDto);

  @Operation(
          summary = "Creates a new user",
          description = "Creates a new user based on the provided data.")
  @PostMapping("/signup_verify")
  ResponseEntity<String> registerUserWithVerify(@Valid @RequestBody CreateUserDto userDto);

  @Operation(
          summary = "Verify user email",
          description = "Verifies email of the user willing to register.")
  @PostMapping("/verify")
  ResponseEntity<?> verifyEmail(@RequestParam("token") String token);
}
