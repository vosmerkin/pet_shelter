package ua.tc.marketplace.exception.auth;

import org.springframework.http.HttpStatus;
import ua.tc.marketplace.exception.model.CustomRuntimeException;

/**
 * {@code VerificationTokenNotFoundOrExpiredException} is a custom runtime exception
 * indicating that the provided email verification token was either not found or has expired.
 * This exception is typically thrown when a user attempts to verify their email
 * using a token that does not exist or is no longer valid.
 *
 * <p>It extends {@link CustomRuntimeException} and is associated with the HTTP
 * status code {@link HttpStatus#GONE} (410), signifying that the requested
 * resource (the verification token) is no longer available.</p>
 */
public class VerificationTokenNotFoundOrExpiredException extends CustomRuntimeException {

  private static final String MESSAGE ="The verification token %s was not found or has expired";
  private static final HttpStatus STATUS = HttpStatus.GONE;

  /**
   * Constructs a new {@code EmailVerificationTokenNotFoundOrExpiredException} with a
   * predefined message and the {@link HttpStatus#GONE} status code.
   */
  public VerificationTokenNotFoundOrExpiredException(String token) {
    super(MESSAGE.formatted(token), STATUS);
  }
}