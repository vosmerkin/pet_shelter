package ua.tc.marketplace.util.openapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.tc.marketplace.controller.UserController;
import ua.tc.marketplace.model.auth.AuthRequest;
import ua.tc.marketplace.model.auth.AuthResponse;
import ua.tc.marketplace.model.auth.PasswordChangeRequest;
import ua.tc.marketplace.model.dto.user.CreateUserDto;

import static ua.tc.marketplace.config.ApiURLs.*;

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
    ResponseEntity<AuthResponse> registerUser(@Valid @ModelAttribute CreateUserDto userDto);

    @Operation(
            summary = "Creates a new user",
            description = "Creates a new user based on the provided data.")
    @PostMapping("/signup_verify")
    ResponseEntity<String> registerUserWithVerify(@Valid @RequestBody CreateUserDto userDto);

    @Operation(
            summary = "Verify user email",
            description = "Verifies email of the user willing to register.")
    @GetMapping("/verify")
    ResponseEntity<?> verifyEmail(@RequestParam("token") String token);


    @Operation(
            summary = "Initiate password reset",
            description = "Sends a password reset token to the user's email address to start the password recovery process.")
    @GetMapping(AUTH_FORGET_PASSWORD)
    ResponseEntity<String> forgetPassword(@RequestParam("email") String email);

    @Operation(
            summary = "Reset user password",
            description = "Resets the user's password using the provided email, password reset token, and new password.")
    @GetMapping(AUTH_VERIFY_PASSWORD_RESET)
    ResponseEntity<Boolean> confirmPasswordReset(String token);

    @Operation(
            summary = "Reset user password",
            description = "Resets the user's password using the provided email, password reset token, and new password.")
    @PutMapping(AUTH_RESET_PASSWORD)
    ResponseEntity<Boolean> reset_password(@RequestBody PasswordChangeRequest request);
}
