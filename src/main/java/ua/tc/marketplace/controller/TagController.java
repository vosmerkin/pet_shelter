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

import static ua.tc.marketplace.config.ApiURLs.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(TAG_BASE)
public class TagController implements TagOpenApi {

  private final TagService tagService;

  @Override
  @GetMapping(TAG_GET_ALL)
  public ResponseEntity<Page<TagDto>> getAllTags(@PageableDefault Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK).body(tagService.findAll(pageable));
  }

  @Override
  @GetMapping(TAG_GET_BY_ID)
  public ResponseEntity<TagDto> getTagById(@PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(tagService.findById(id));
  }

  @Override
  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping(TAG_CREATE)
  public ResponseEntity<TagDto> createTag(@RequestBody @Valid CreateTagDto dto) {
    return ResponseEntity.ok(tagService.createTag(dto));
  }

  @Override
  @PreAuthorize("hasAuthority('ADMIN')")
  @PutMapping(TAG_UPDATE)
  public ResponseEntity<TagDto> updateTag(@PathVariable Long id, @RequestBody @Valid UpdateTagDto tagDto) {
    return ResponseEntity.status(HttpStatus.OK).body(tagService.updateTag(id, tagDto));
  }

  @Override
  @PreAuthorize("hasAuthority('ADMIN')")
  @DeleteMapping(TAG_DELETE)
  public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
    tagService.deleteTagById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }


}
