package ua.tc.marketplace.exception.article;

import org.springframework.http.HttpStatus;
import ua.tc.marketplace.exception.model.CustomRuntimeException;

/**
 * Exception thrown when an article slug is already in use.
 *
 * <p>This exception is a subclass of {@link CustomRuntimeException} and is used to signal that an
 * article slug cannot be used because it is already taken. It carries a predefined error message and
 * an HTTP status code of CONFLICT (409).
 *
 * <p>The error message includes the slug that is in use.
 */
public class ArticleSlugInUseException extends CustomRuntimeException {

  private static final String ERROR_MESSAGE = "Article slug '%s' is already in use.";
  private static final HttpStatus STATUS = HttpStatus.CONFLICT;

  public ArticleSlugInUseException(String slug) {
    super(ERROR_MESSAGE.formatted(slug), STATUS);
  }
}
