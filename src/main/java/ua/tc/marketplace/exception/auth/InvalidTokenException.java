package ua.tc.marketplace.exception.auth;

import org.springframework.http.HttpStatus;
import ua.tc.marketplace.exception.model.CustomRuntimeException;

public class InvalidTokenException extends CustomRuntimeException {

  private static final String MESSAGE ="The token is invalid";
  private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

  public InvalidTokenException() {
    super(MESSAGE, STATUS);
  }
}
