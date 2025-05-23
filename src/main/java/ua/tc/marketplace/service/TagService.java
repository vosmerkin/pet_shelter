package ua.tc.marketplace.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.tc.marketplace.model.dto.tag.CreateTagDto;
import ua.tc.marketplace.model.dto.tag.TagDto;
import ua.tc.marketplace.model.dto.tag.UpdateTagDto;
import ua.tc.marketplace.model.entity.Tag;

public interface TagService {

  Page<TagDto> findAll(Pageable pageable);

  TagDto findById(Long id);

  Tag getById(Long id);

  TagDto createTag(CreateTagDto createTagDto);

  TagDto updateTag(Long id, UpdateTagDto updateTagDto);

  void deleteTagById(Long id);
}
