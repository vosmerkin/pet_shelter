package ua.tc.marketplace.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.tc.marketplace.model.dto.category.CategoryCountedDto;
import ua.tc.marketplace.model.dto.category.CategoryDto;
import ua.tc.marketplace.model.dto.category.CreateCategoryDto;
import ua.tc.marketplace.model.dto.category.UpdateCategoryDto;
import ua.tc.marketplace.model.entity.Attribute;
import ua.tc.marketplace.model.entity.Category;
import ua.tc.marketplace.model.entity.CategoryAttribute;

import java.util.Set;

/**
 * Service interface defining operations for managing categories' attributes. Includes methods for
 * retrieving, creating, updating, and deleting category attributes.
 *
 */
public interface CategoryAttributeService {
    Set<Attribute> findAttributesByCategoryId(Long categoryId);

//    Page<CategoryDto> findAll(Pageable pageable);

//    CategoryDto findById(Long id);
    Set<CategoryAttribute> findByCategoryId (Long categoryId);
    CategoryAttribute updateValues(CategoryAttribute categoryAttribute, Set<String> values);

     



    Category addAttributeToCategory(Attribute attribute);
    Category removeAttributeFromCategory(Attribute attribute);

    CategoryDto createCategory(CreateCategoryDto categoryDto);

    CategoryDto update(Long id, UpdateCategoryDto categoryDto);

    void deleteById(Long id);

    Page<CategoryCountedDto> findAllCounted(Pageable pageable);
}
