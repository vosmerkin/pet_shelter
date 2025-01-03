package ua.tc.marketplace.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.tc.marketplace.model.dto.category.CategoryCountedDto;
import ua.tc.marketplace.model.dto.category.CategoryDto;
import ua.tc.marketplace.model.dto.category.CreateCategoryDto;
import ua.tc.marketplace.model.dto.category.UpdateCategoryDto;
import ua.tc.marketplace.model.entity.Category;

/**
 * Service interface defining operations for managing categories. Includes methods for
 * retrieving, creating, updating, and deleting categories.
 *
 * <p>This interface provides operations for:
 * <ul>
 *   <li>Retrieving a category by its ID</li>
 *   <li>Retrieving all categories with pagination support</li>
 *   <li>Creating a new category</li>
 *   <li>Updating an existing category</li>
 *   <li>Deleting a category by its ID</li>
 * </ul>
 * </p>
 */
public interface CategoryService {
    Category findCategoryById(Long categoryId);

    Page<CategoryDto> findAll(Pageable pageable);

    CategoryDto findById(Long id);

    CategoryDto createCategory(CreateCategoryDto categoryDto);

    CategoryDto update(Long id, UpdateCategoryDto categoryDto);

    void deleteById(Long id);

    Page<CategoryCountedDto> findAllCounted(Pageable pageable);
}
