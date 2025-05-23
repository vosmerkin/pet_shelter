package ua.tc.marketplace.model.entity;

import java.util.ArrayList;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ua.tc.marketplace.model.enums.ArticleStatus;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Represents an article in the marketplace platform.
 * Articles can be written by users, contain multimedia content,
 * and are categorized for better discoverability.
 */
@Entity
@Table(name = "article")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Article entity representing blog posts or content pieces in the marketplace")
public class Article {
  /**
   * Unique identifier for the article
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(description = "Unique identifier of the article",
          example = "1")
  private Long id;

  /**
   * Title of the article
   */
  @Column(nullable = false)
  @Schema(description = "Title of the article",
          example = "Marketplace Trends 2023")
  @NotNull
  private String title;

  /**
   * Main content of the article in HTML or plain text format
   */
  @Column(nullable = false, columnDefinition = "TEXT")
  @Schema(description = "Main content of the article",
          example = "<p>This is the article content...</p>")
  @NotNull
  private String content;

  /**
   * Author who created the article
   */
  @ManyToOne
  @JoinColumn(name = "author_id", nullable = false)
  @Schema(description = "Author of the article")
  private User author;

  /**
   * Timestamp when the article was created
   */
  @CreationTimestamp
  @Column(updatable = false)
  @Schema(description = "Timestamp when the article was created", example = "2023-01-01T12:00:00")
  private LocalDateTime createdAt;

  /**
   * Timestamp when the article was last updated
   */
  @UpdateTimestamp
  @Schema(description = "Timestamp when the article was last updated", example = "2023-01-02T15:30:00")
  private LocalDateTime updatedAt;

  /**
   * List of photos associated with the article
   */
  @Builder.Default
  @ManyToMany
  @JoinTable(
          name = "article_photo",
          joinColumns = @JoinColumn(name = "article_id"),
          inverseJoinColumns = @JoinColumn(name = "photo_id")
  )
  @Schema(description = "List of photos attached to the article")
  private List<Photo> photos = new ArrayList<>();

  /**
   * List of tags associated with the article for categorization
   */
  @Builder.Default
  @ManyToMany
  @JoinTable(
          name = "article_tag",
          joinColumns = @JoinColumn(name = "article_id"),
          inverseJoinColumns = @JoinColumn(name = "tag_id")
  )
  @Schema(description = "List of tags associated with the article")
  private List<Tag> tags = new ArrayList<>();

  /**
   * Category the article belongs to
   */
  @ManyToOne
  @JoinColumn(name = "category_id", nullable = false)
  @Schema(description = "Category of the article")
  @NotNull
  private Category category;

  /**
   * SEO meta description for the article
   */
  @Schema(description = "SEO meta description for search engines",
          example = "Learn about the latest marketplace trends in 2023")
  private String metaDescription;

  /**
   * Structured data for SEO (JSON-LD format)
   */
  @Schema(description = "Structured data in JSON-LD format for SEO",
          example = "{\"@context\":\"https://schema.org\",\"@type\":\"NewsArticle\"...}")
  private String structuredData;

  /**
   * Current status of the article (DRAFT, PUBLISHED, ARCHIVED)
   */
  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Schema(description = "Publication status of the article",
          example = "PUBLISHED")
  @NotNull
  private ArticleStatus status=ArticleStatus.DRAFT;

  /**
   * Flag indicating if the article is featured
   */
  @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
  @Builder.Default
  @Schema(description = "Whether the article is featured",
          example = "false")
  private Boolean isFeatured = false;

  /**
   * Number of likes the article has received
   */
  @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
  @Builder.Default
  @Schema(description = "Number of likes the article has received",
          example = "42")
  private Integer likes = 0;

  /**
   * SEO-friendly URL slug
   */
  @Schema(description = "SEO-friendly URL slug",
          example = "marketplace-trends-2023")
  private String slug;
}