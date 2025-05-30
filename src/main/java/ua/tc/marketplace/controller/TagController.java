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
import ua.tc.marketplace.model.dto.tag.CreateTagDto;
import ua.tc.marketplace.model.dto.tag.TagDto;
import ua.tc.marketplace.model.dto.tag.UpdateTagDto;
import ua.tc.marketplace.service.TagService;
import ua.tc.marketplace.util.openapi.TagOpenApi;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tag")
public class TagController implements TagOpenApi {

  private final TagService tagService;

  @Override
  @GetMapping("/all")
  public ResponseEntity<Page<TagDto>> getAllTags(@PageableDefault Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK).body(tagService.findAll(pageable));
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<TagDto> getTagById(@PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(tagService.findById(id));
  }

  @Override
  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping
  public ResponseEntity<TagDto> createTag(@RequestBody @Valid CreateTagDto dto) {
    return ResponseEntity.ok(tagService.createTag(dto));
  }

  @Override
  @PreAuthorize("hasAuthority('ADMIN')")
  @PutMapping("/{id}")
  public ResponseEntity<TagDto> updateTag(@PathVariable Long id, @RequestBody @Valid UpdateTagDto tagDto) {
    return ResponseEntity.status(HttpStatus.OK).body(tagService.updateTag(id, tagDto));
  }

  @Override
  @PreAuthorize("hasAuthority('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
    tagService.deleteTagById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }


}
