package ua.tc.marketplace.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.tc.marketplace.model.dto.article.ArticleDto;
import ua.tc.marketplace.model.dto.article.CreateArticleDto;
import ua.tc.marketplace.model.dto.article.UpdateArticleDto;

public interface ArticleService {

  /**
   * Retrieves all articles, paginated.
   * @param pageable pagination information
   * @return a paginated list of articles
   */
  Page<ArticleDto> findAll(Pageable pageable);

  /**
   * Retrieves a single article by its ID.
   * @param id the article's unique identifier
   * @return the article details
   */
  ArticleDto findById(Long id);

  /**
   * Creates a new article.
   * @param createArticleDto the data transfer object containing article details
   * @return the created article
   */
  ArticleDto create(CreateArticleDto createArticleDto);

  /**
   * Updates an existing article.
   * @param id the article's unique identifier
   * @param updateArticleDto the data transfer object containing the updated article details
   * @return the updated article
   */
  ArticleDto update(Long id, UpdateArticleDto updateArticleDto);

  /**
   * Deletes an article by its ID.
   * @param id the article's unique identifier
   */
  void deleteById(Long id);
}

