package ua.tc.marketplace.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.tc.marketplace.config.ApiURLs;
import ua.tc.marketplace.model.dto.category.*;
import ua.tc.marketplace.service.CategoryAttributeService;
import ua.tc.marketplace.service.CategoryService;
import ua.tc.marketplace.util.openapi.CategoryOpenApi;

import java.util.Set;
import java.util.stream.Collectors;

import static ua.tc.marketplace.config.ApiURLs.*;

/**
 * CategoryController handles HTTP requests related to CRUD operations for the Category entity. It
 * provides an API for retrieving, creating, updating, and deleting categories.
 */
@RestController
@RequestMapping(CATEGORY_BASE)
@RequiredArgsConstructor
@Slf4j
public class CategoryController implements CategoryOpenApi {

  private final CategoryService categoryService;
  private final CategoryAttributeService categoryAttributeService;

  @Override
  @GetMapping(CATEGORY_GET_ALL)
  public ResponseEntity<Page<CategoryDto>> getAllCategories(Pageable pageable) {
    log.info("Request to get all categories");
    Page<CategoryDto> categories = categoryService.findAll(pageable);
    log.info("Categories was get successfully");
    return ResponseEntity.ok(categories);
  }

  @Override
  @GetMapping(CATEGORY_GET_ALL_COUNTED)
  public ResponseEntity<Page<CategoryCountedDto>> getAllCountedCategories(Pageable pageable) {
    log.info("Request to get all categories with ads counts");
    Page<CategoryCountedDto> categories = categoryService.findAllCounted(pageable);
    log.info("Categories with ads counts was get successfully");
    return ResponseEntity.ok(categories);
  }

  @Override
  @GetMapping(CATEGORY_BY_ID)
  public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
    log.info("Request to get category with ID: {}", id);
    CategoryDto categoryDto = categoryService.findById(id);
    log.info("Category with ID: {} got successfully", id);
    return ResponseEntity.ok(categoryDto);
  }

  @Override
  @GetMapping(CATEGORY_ATTRIBUTE_BY_IDS)
  public ResponseEntity<CategoryAttributeDto> getCategoryAttributeByIds(
      @PathVariable Long categoryId, @PathVariable Long attributeId) {
    log.info(
        "Request to get category attribute " + "with category ID: {}, and attribute ID: {}",
        categoryId,
        attributeId);
    CategoryAttributeDto categoryAttribute =
        categoryAttributeService.findCategoryAttributeByIds(categoryId, attributeId);
    log.debug("Returning requested category attribute: {}", categoryAttribute.toString());
    return ResponseEntity.ok(categoryAttribute);
  }

  @Override
  @GetMapping(CATEGORY_ATTRIBUTES_BY_CATEGORY_ID)
  public ResponseEntity<Set<CategoryAttributeDto>> getCategoryAttributesByCategoryId(
      @PathVariable Long categoryId) {
    log.info("Request to get category attributes " + "by category ID: {}", categoryId);
    Set<CategoryAttributeDto> categoryAttributes =
        categoryAttributeService.getCategoryAttributesByCategoryId(categoryId);
    log.debug(
        "Returning requested category attributes: {}",
        categoryAttributes.stream()
            .map(CategoryAttributeDto::toString) // or customize mapping
            .collect(Collectors.toSet()));
    return ResponseEntity.ok(categoryAttributes);
  }

  @Override
  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping(CATEGORY_CREATE)
  public ResponseEntity<CategoryDto> createCategory(
      @Valid @RequestBody CreateCategoryDto categoryDTO) {
    log.info("Request to create category ");
    CategoryDto createCategory = categoryService.createCategory(categoryDTO);
    log.info("Category was create successfully");
    return ResponseEntity.status(HttpStatus.CREATED).body(createCategory);
  }

  @Override
  @PreAuthorize("hasAuthority('ADMIN')")
  @PutMapping(CATEGORY_UPDATE)
  public ResponseEntity<CategoryDto> updateCategory(
      @PathVariable Long id, @RequestBody UpdateCategoryDto categoryDto) {
    log.info("Request to update category with ID: {}", id);
    CategoryDto updatedCategory = categoryService.update(id, categoryDto);
    log.info("Category with ID: {} updated successfully", id);
    return ResponseEntity.ok(updatedCategory);
  }

  @Override
  //  @PreAuthorize("hasAuthority('ADMIN')")
  @PutMapping(CATEGORY_ATTRIBUTE_UPDATE)
  public ResponseEntity<CategoryAttributeDto> updateCategoryAttribute(
      @PathVariable Long categoryId,
      @PathVariable Long attributeId,
      @RequestBody UpdateCategoryAttributeDto categoryDto) {
    log.info(
        "Request to update category attribute options "
            + "by categoryId={} and attributeId={}, with values {}",
        categoryId,
        attributeId,
        categoryDto);
    CategoryAttributeDto categoryAttribute =
        categoryAttributeService.updateCategoryAttribute(categoryId, attributeId, categoryDto);
    log.debug("Successfully updated category attributevalues: {}", categoryAttribute.toString());
    return ResponseEntity.ok(categoryAttribute);
  }

  @Override
  @PreAuthorize("hasAuthority('ADMIN')")
  @DeleteMapping(CATEGORY_DELETE)
  public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
    log.info("Request to delete category with ID: {}", id);
    categoryService.deleteById(id);
    log.info("Category with ID: {} deleted successfully", id);
    return ResponseEntity.noContent().build();
  }
}
