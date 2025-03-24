package ua.tc.marketplace.exception.article;

import org.springframework.http.HttpStatus;
import ua.tc.marketplace.exception.model.CustomRuntimeException;

/**
 * Exception thrown when an article is not found in the system.
 *
 * <p>This exception is a subclass of {@link CustomRuntimeException} and is used to signal that an
 * article with a specific ID does not exist. It carries a predefined error message and an HTTP status
 * code of NOT_FOUND (404).
 *
 * <p>The error message includes the article ID that was not found.
 */
public class ArticleNotFoundException extends CustomRuntimeException {

  private static final String ERROR_MESSAGE = "Article with id %s is not found.";
  private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

  public ArticleNotFoundException(Long articleId) {
    super(ERROR_MESSAGE.formatted(articleId), STATUS);
  }
}
