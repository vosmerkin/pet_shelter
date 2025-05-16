package ua.tc.marketplace.exception.auth;

import org.springframework.http.HttpStatus;
import ua.tc.marketplace.exception.model.CustomRuntimeException;

/**
 * Exception thrown when an attempt is made to register an email address that is already
 * pending email verification.
 *
 * <p>This exception is a subclass of {@link CustomRuntimeException} and is used to signal that
 * a user with the provided email address has already registered but has not yet verified
 * their email. It carries a predefined error message and an HTTP status code of
 * {@link HttpStatus#CONFLICT} (409), indicating a conflict with the current state of the
 * resource (an unverified user with that email already exists).</p>
 *
 * <p>The error message includes the email address that is already pending verification.</p>
 */
public class EmailAlreadyPendingVerificationException extends CustomRuntimeException {

  private static final String ERROR_MESSAGE = "Email address '%s' is already pending verification.";
  private static final HttpStatus STATUS = HttpStatus.CONFLICT;

  /**
   * Constructs a new {@code EmailAlreadyPendingVerificationException} with the email address
   * that is already pending verification.
   *
   * @param email The email address that caused the exception.
   */
  public EmailAlreadyPendingVerificationException(String email) {
    super(ERROR_MESSAGE.formatted(email), STATUS);
  }
}