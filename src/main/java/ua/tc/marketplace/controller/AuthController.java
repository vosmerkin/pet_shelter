package ua.tc.marketplace.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.tc.marketplace.model.auth.AuthRequest;
import ua.tc.marketplace.model.auth.AuthResponse;
import ua.tc.marketplace.model.dto.user.CreateUserDto;
import ua.tc.marketplace.service.AuthenticationService;
import ua.tc.marketplace.util.MailService;
import ua.tc.marketplace.util.openapi.AuthOpenApi;

import static ua.tc.marketplace.config.ApiURLs.*;

@Slf4j
@RestController
@RequestMapping(AUTH_BASE)
@RequiredArgsConstructor
public class AuthController implements AuthOpenApi {

  private final AuthenticationService authenticationService;
  private final MailService mailService;

  @Override
  @PostMapping("/login")
  public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest authRequest) {
    log.info("Login request: {}", authRequest);
    return ResponseEntity.ok(authenticationService.authenticate(authRequest));
  }

  @Override
  @PostMapping("/signup")
  public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody CreateUserDto userDto) {
    log.info("Register user request: {}", userDto);
    return ResponseEntity.status(HttpStatus.OK).body(authenticationService.registerUser(userDto));
  }

  @Override
  @PostMapping("/signup_verify")
  public ResponseEntity<Void> registerUserWithVerify(@Valid @RequestBody CreateUserDto userDto) {
    log.info("Register user with verification request: {}", userDto);
    authenticationService.registerUserWithVerify(userDto);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/test_email")
  public ResponseEntity<Void> testEmail() {
    log.info("Test email: {}", "userDto");
//    mailService.sendVerificationEmail();
    mailService.sendVerificationEmail("vosmerkin.evgen1@gmail.com", "sdfsdfsdfsdfs");
    return ResponseEntity.ok().build();
  }

  @GetMapping(AUTH_VERIFY_EMAIL)
  public ResponseEntity<Boolean> verifyEmail(@PathVariable String token) {
    log.info("Verify user request: {}", token);
//    Optional<UnverifiedUser> unverifiedUser = registrationService.findByVerificationToken(token);
//    if (unverifiedUser.isPresent()) {
//      // Redirect to a frontend page with a form to complete registration
//      return ResponseEntity.ok("Email verified. Please complete your registration.");
//      // Or, you could directly return the unverified user's email if needed on the frontend
//      // return ResponseEntity.ok(Map.of("email", unverifiedUser.getEmail(), "token", token));
//    } else {
//      return ResponseEntity.badRequest().body("Invalid or expired verification token");
//    }
    return ResponseEntity.status(HttpStatus.OK).body(authenticationService.verifyEmail(token));
  }
}
