package ua.tc.marketplace.exception.auth;

import org.springframework.http.HttpStatus;
import ua.tc.marketplace.exception.model.CustomRuntimeException;

public class ExpiredTokenException extends CustomRuntimeException {

  private static final String MESSAGE ="The token has expired";
  private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

  public ExpiredTokenException() {
    super(MESSAGE, STATUS);
  }
}
