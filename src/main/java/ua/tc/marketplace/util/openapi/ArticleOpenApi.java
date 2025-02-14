package ua.tc.marketplace.util.openapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.tc.marketplace.model.dto.article.CreateArticleDto;
import ua.tc.marketplace.model.dto.article.ArticleDto;
import ua.tc.marketplace.model.dto.article.UpdateArticleDto;

/**
 * This interface defines the OpenAPI annotations for the {@link ua.tc.marketplace.controller.ArticleController} class.
 * It provides endpoints for managing articles.
 */
@Tag(name = "Article API", description = "API for managing articles related to pets")
public interface ArticleOpenApi {

  @Operation(
          summary = "Get all articles",
          description = "Retrieves a pageable list of all articles about pets, fostering, adoption, etc.")
  @GetMapping
  ResponseEntity<Page<ArticleDto>> getAllArticles(@PageableDefault Pageable pageable);

  @Operation(
          summary = "Get article by ID",
          description = "Retrieves a specific article by its unique identifier.")
  @GetMapping("/{id}")
  ResponseEntity<ArticleDto> getArticleById(@PathVariable Long id);

  @Operation(
          summary = "Create a new article",
          description = "Creates a new article for pet adoption, fostering, or related topics.")
  @PostMapping
  ResponseEntity<ArticleDto> createArticle(@Valid @RequestBody CreateArticleDto dto);

  @Operation(
          summary = "Update an existing article",
          description = "Updates an existing article with new data for content, title, etc.")
  @PutMapping("/{id}")
  ResponseEntity<ArticleDto> updateArticle(@PathVariable Long id, @RequestBody @Valid UpdateArticleDto dto);

  @Operation(
          summary = "Delete an article",
          description = "Deletes a specific article based on its unique identifier.")
  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteArticle(@PathVariable Long id);
}
