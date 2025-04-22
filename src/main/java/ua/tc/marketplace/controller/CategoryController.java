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
import ua.tc.marketplace.model.dto.category.CategoryCountedDto;
import ua.tc.marketplace.model.dto.category.CategoryDto;
import ua.tc.marketplace.model.dto.category.CreateCategoryDto;
import ua.tc.marketplace.model.dto.category.UpdateCategoryDto;
import ua.tc.marketplace.service.CategoryService;
import ua.tc.marketplace.util.openapi.CategoryOpenApi;

/**
 * CategoryController handles HTTP requests related to CRUD operations for the Category entity. It
 * provides an API for retrieving, creating, updating, and deleting categories.
 */
@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
@Slf4j
public class CategoryController implements CategoryOpenApi {

  private final CategoryService categoryService;

  @Override
  @GetMapping
  public ResponseEntity<Page<CategoryDto>> getAllCategories(Pageable pageable) {
    log.info("Request to get all categories");
    Page<CategoryDto> categories = categoryService.findAll(pageable);
    log.info("Categories was get successfully");
    return ResponseEntity.ok(categories);
  }

  @Override
  @GetMapping("/counted")
  public ResponseEntity<Page<CategoryCountedDto>> getAllCountedCategories(Pageable pageable) {
    log.info("Request to get all categories with ads counts");
    Page<CategoryCountedDto> categories = categoryService.findAllCounted(pageable) ;
    log.info("Categories with ads counts was get successfully");
    return ResponseEntity.ok(categories);
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
    log.info("Request to get category with ID: {}", id);
    CategoryDto categoryDto = categoryService.findById(id);
    log.info("Category with ID: {} got successfully", id);
    return ResponseEntity.ok(categoryDto);
  }

  @Override
  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping
  public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CreateCategoryDto categoryDTO) {
    log.info("Request to create category ");
    CategoryDto createCategory = categoryService.createCategory(categoryDTO);
    log.info("Category was create successfully");
    return ResponseEntity.status(HttpStatus.CREATED).body(createCategory);
  }

  @Override
  @PreAuthorize("hasAuthority('ADMIN')")
  @PutMapping("/{id}")
  public ResponseEntity<CategoryDto> updateCategory(
      @PathVariable Long id, @RequestBody UpdateCategoryDto categoryDto) {
    log.info("Request to update category with ID: {}", id);
    CategoryDto updatedCategory = categoryService.update(id, categoryDto);
    log.info("Category with ID: {} updated successfully", id);
    return ResponseEntity.ok(updatedCategory);
  }

  @Override
  @PreAuthorize("hasAuthority('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
    log.info("Request to delete category with ID: {}", id);
    categoryService.deleteById(id);
    log.info("Category with ID: {} deleted successfully", id);
    return ResponseEntity.noContent().build();
  }
}
