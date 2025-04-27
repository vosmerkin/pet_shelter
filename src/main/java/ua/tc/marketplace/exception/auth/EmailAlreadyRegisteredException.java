package ua.tc.marketplace.exception.auth;

import org.springframework.http.HttpStatus;
import ua.tc.marketplace.exception.model.CustomRuntimeException;

/**
 * Exception thrown when an attempt is made to register an email address that is already in use.
 *
 * <p>This exception is a subclass of {@link CustomRuntimeException} and is used to signal that
 * a user with the provided email address already exists in the system. It carries a
 * predefined error message and an HTTP status code of CONFLICT (409), indicating a conflict
 * with the current state of the resource.</p>
 *
 * <p>The error message includes the email address that is already registered.</p>
 */
public class EmailAlreadyRegisteredException extends CustomRuntimeException {

  private static final String ERROR_MESSAGE = "Email address '%s' is already registered.";
  private static final HttpStatus STATUS = HttpStatus.CONFLICT; // Corrected status code

  /**
   * Constructs a new {@code EmailAlreadyRegisteredException} with the email address that
   * is already registered.
   *
   * @param email The email address that caused the exception.
   */
  public EmailAlreadyRegisteredException(String email) {
    super(ERROR_MESSAGE.formatted(email), STATUS);
  }
}