package ua.tc.marketplace.model.dto.article;

import io.swagger.v3.oas.annotations.media.Schema;
import ua.tc.marketplace.model.enums.ArticleStatus;

import java.util.List;

/**
 * Data Transfer Object (DTO) for updating an article.
 */
public record UpdateArticleDto(
        @Schema(example = "How to Help Stray Animals") String title,
        @Schema(example = "Providing food and shelter for stray animals can make a big difference...") String content,
        Long categoryId,
        List<Long> tagIds,
        List<Long> photoIds,
        @Schema(example = "Ways to support and care for stray animals in your community") String metaDescription,
        String structuredData,
        ArticleStatus status,
        @Schema(example = "false") Boolean isFeatured,
        @Schema(example = "how-to-adopt-a-rescue-dog") String slug
) {
}
