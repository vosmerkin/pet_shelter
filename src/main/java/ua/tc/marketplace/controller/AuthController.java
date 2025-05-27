package ua.tc.marketplace.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.tc.marketplace.config.ApiURLs;
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

  @Value("${verification.mail.baseurl}")
  private String baseUrl;

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
  public ResponseEntity<String> registerUserWithVerify(@Valid @RequestBody CreateUserDto userDto) {
    log.info("Register user with verification request: {}", userDto);
    String token=authenticationService.registerUserWithVerify(userDto);
//    return ResponseEntity.ok().build();
    String message = "Click the following link to verify your email: " +
            baseUrl +
            ApiURLs.AUTH_BASE +
            ApiURLs.AUTH_VERIFY_EMAIL+
            token;
    return ResponseEntity.status(HttpStatus.OK).body(message);
  }

  @GetMapping("/test_email")
  public ResponseEntity<Void> testEmail() {
    log.info("Test email: {}", "userDto");
    mailService.sendVerificationEmailResend("vosmerkin.evgen1@gmail.com", "resend_sdfsdfsdfsdfs");
    mailService.sendVerificationEmailJavaMailSender("vosmerkin.evgen1@gmail.com", "JavaMailSender_sdfsdfsdfsdfs");
    return ResponseEntity.ok().build();
  }

//  @GetMapping(AUTH_VERIFY_EMAIL)
  @GetMapping( "/verify-email")
  public ResponseEntity<Boolean> verifyEmail(@RequestParam("token") String token) {
//  public ResponseEntity<Boolean> verifyEmail(@PathVariable String token) {
    log.info("Verify email request: {}", token);
    return ResponseEntity.status(HttpStatus.OK).body(authenticationService.verifyEmail(token));
  }



}
