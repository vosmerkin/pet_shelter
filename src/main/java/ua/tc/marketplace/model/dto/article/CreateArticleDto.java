package ua.tc.marketplace.model.dto.article;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import ua.tc.marketplace.model.enums.ArticleStatus;

/**
 * Data Transfer Object (DTO) for creating a new article.
 */
public record CreateArticleDto(
        @NotBlank @Schema(example = "How to Foster a Kitten") String title,
        @NotBlank @Schema(example = "Fostering a kitten can help shelters save more lives...") String content,
        @NotNull Long authorId,
        @NotNull Long categoryId,
        List<Long> tagIds,
        List<Long> photoIds,
        @Schema(example = "A step-by-step guide to fostering kittens") String metaDescription,
        String structuredData,
        @NotNull ArticleStatus status,
        @Schema(example = "true") Boolean isFeatured,
        @Schema(example = "how-to-adopt-a-rescue-dog") String slug
) {}
