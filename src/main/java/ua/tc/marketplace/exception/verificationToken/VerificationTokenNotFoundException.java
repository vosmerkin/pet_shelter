package ua.tc.marketplace.exception.verificationToken;

import org.springframework.http.HttpStatus;
import ua.tc.marketplace.exception.model.CustomRuntimeException;

/**
 * Exception thrown when a verification token is not found in the system.
 *
 * <p>This exception is a subclass of {@link CustomRuntimeException} and is used to signal that a
 * verification token with a specific ID does not exist. It carries a predefined error message and an HTTP status
 * code of NOT_FOUND (404).
 *
 * <p>The error message includes the verification token ID that was not found.
 */
public class VerificationTokenNotFoundException extends CustomRuntimeException {

  private static final String ERROR_MESSAGE_BY_ID = "VerificationToken with id %s not found.";
  private static final String ERROR_MESSAGE_BY_STRING = "VerificationToken with token string %s not found.";
  private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

  public VerificationTokenNotFoundException(Long tokenId) {
    super(ERROR_MESSAGE_BY_ID.formatted(tokenId), STATUS);
  }
  public VerificationTokenNotFoundException(String token) {
    super(ERROR_MESSAGE_BY_STRING.formatted(token), STATUS);
  }
}