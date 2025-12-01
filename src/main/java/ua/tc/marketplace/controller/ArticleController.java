package ua.tc.marketplace.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.tc.marketplace.model.dto.article.ArticleDto;
import ua.tc.marketplace.model.dto.article.CreateArticleDto;
import ua.tc.marketplace.model.dto.article.UpdateArticleDto;
import ua.tc.marketplace.service.ArticleService;
import ua.tc.marketplace.util.openapi.ArticleOpenApi;

import static ua.tc.marketplace.config.ApiURLs.*;

/**
 * REST controller for managing articles.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(ARTICLE_BASE)
public class ArticleController implements ArticleOpenApi {

  private final ArticleService articleService;

  @Override
  @GetMapping(ARTICLE_GET_ALL)
  public ResponseEntity<Page<ArticleDto>> getAllArticles(@PageableDefault Pageable pageable) {
    return ResponseEntity.ok(articleService.findAll(pageable));
  }

  @Override
  @GetMapping(ARTICLE_GET_BY_ID)
  public ResponseEntity<ArticleDto> getArticleById(@PathVariable Long id) {
    return ResponseEntity.ok(articleService.findById(id));
  }

  @Override
  @PreAuthorize("@securityService.hasAnyRoleAndOwnership(#dto.authorId)")
  @PostMapping(ARTICLE_CREATE)
  public ResponseEntity<ArticleDto> createArticle(@RequestBody @Valid CreateArticleDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(articleService.create(dto));
  }

  @Override
  @PreAuthorize("@securityService.hasAnyRoleAndOwnership(#dto.authorId)")
  @PutMapping(ARTICLE_UPDATE)
  public ResponseEntity<ArticleDto> updateArticle(@PathVariable Long id, @RequestBody @Valid UpdateArticleDto dto) {
    return ResponseEntity.ok(articleService.update(id, dto));
  }

  @Override
  @PreAuthorize("hasAuthority('ADMIN') or @securityService.hasAnyRoleAndOwnership(#articleService.findById(id).authorId)")
  @DeleteMapping(ARTICLE_DELETE)
  public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
    articleService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
