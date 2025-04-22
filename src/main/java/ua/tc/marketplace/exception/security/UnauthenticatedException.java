package ua.tc.marketplace.exception.security;

import org.springframework.http.HttpStatus;
import ua.tc.marketplace.exception.model.CustomRuntimeException;

/**
 * Exception thrown when a user is not authenticated.
 * <p>
 * This exception extends {@link CustomRuntimeException} and represents
 * an authentication failure scenario.
 * </p>
 */
public class UnauthenticatedException extends CustomRuntimeException {

  private static final String ERROR_MESSAGE = "User not authenticated";
  private static final HttpStatus STATUS = HttpStatus.CONFLICT;

  /**
   * Constructs a new {@code UnauthenticatedExceptionException} with a default error message
   * and HTTP status code {@link HttpStatus#CONFLICT}.
   */
  public UnauthenticatedException() {
    super(ERROR_MESSAGE, STATUS);
  }
}
