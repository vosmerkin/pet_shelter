package ua.tc.marketplace.exception.auth;

import org.springframework.http.HttpStatus;
import ua.tc.marketplace.exception.model.CustomRuntimeException;

public class MissingTokenException extends CustomRuntimeException {

  private static final String MESSAGE ="No token provided in the request";
  private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

  public MissingTokenException() {
    super(MESSAGE, STATUS);
  }
}
