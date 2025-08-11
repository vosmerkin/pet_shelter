package ua.tc.marketplace.exception.category;

import org.springframework.http.HttpStatus;
import ua.tc.marketplace.exception.model.CustomRuntimeException;

/**
 * Custom exception class for handling cases where a category attribute with a specific categoryId
 * and attributeId is not found. Extends CustomRuntimeException and sets an appropriate HTTP status
 * code (404 - Not Found).
 */
public class CategoryAttributeNotFoundException extends CustomRuntimeException {

  private static final String ERROR_MESSAGE_BY_CATEGORY_ID_AND_ATTRIBUTE_ID =
      "Category attribute with categoryId %s and attributeId %s is not found.";
  private static final String ERROR_MESSAGE_BY_CATEGORY_ID =
          "Category attribute with categoryId %s is not found.";
  private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

  public CategoryAttributeNotFoundException(Long categoryId, Long attributeId) {
    super(ERROR_MESSAGE_BY_CATEGORY_ID_AND_ATTRIBUTE_ID.formatted(categoryId, attributeId), STATUS);
  }
  public CategoryAttributeNotFoundException(Long categoryId) {
    super(ERROR_MESSAGE_BY_CATEGORY_ID.formatted(categoryId), STATUS);
  }
}
