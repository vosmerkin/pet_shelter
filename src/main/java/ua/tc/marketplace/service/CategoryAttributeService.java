package ua.tc.marketplace.service;

import ua.tc.marketplace.model.dto.category.CategoryAttributeDto;
import ua.tc.marketplace.model.dto.category.UpdateCategoryAttributeDto;

import java.util.Set;

/**
 * Service interface defining operations for managing categories' attributes. Includes methods for
 * retrieving, creating, updating, and deleting category attributes.
 *
 * <p>This interface provides operations for:
 * <ul>
 *   <li>Retrieving a category attribute by its IDs</li>
 *   <li>Retrieving all category attributes by category id</li>
 *   <li>Updating an category attribute options</li>
 * </ul>
 * </p>
 */
public interface CategoryAttributeService {
    CategoryAttributeDto findCategoryAttributeByIds(Long categoryId, Long attributeId);

    Set<CategoryAttributeDto> getCategoryAttributesByCategoryId(Long categoryId);

    CategoryAttributeDto updateCategoryAttribute(Long categoryId,
                                                 Long attributeId,
                                                 UpdateCategoryAttributeDto categoryDto);
}
