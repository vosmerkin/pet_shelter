package ua.tc.marketplace.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.tc.marketplace.exception.category.CategoryAttributeNotFoundException;
import ua.tc.marketplace.model.dto.category.CategoryAttributeDto;
import ua.tc.marketplace.model.dto.category.UpdateCategoryAttributeDto;
import ua.tc.marketplace.model.entity.CategoryAttribute;
import ua.tc.marketplace.repository.CategoryAttributeRepository;
import ua.tc.marketplace.service.CategoryAttributeService;
import ua.tc.marketplace.util.mapper.CategoryAttributeMapper;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryAttributeServiceImpl implements CategoryAttributeService {
  private final CategoryAttributeRepository repository;
  private final CategoryAttributeMapper mapper;

  @Override
  public CategoryAttributeDto findCategoryAttributeByIds(Long categoryId, Long attributeId) {
    CategoryAttribute categoryAttribute =
        repository
            .findByCategory_IdAndAttribute_Id(categoryId, attributeId)
            .orElseThrow(() -> new CategoryAttributeNotFoundException(categoryId, attributeId));
    return mapper.toDto(categoryAttribute);
  }

  @Override
  public Set<CategoryAttributeDto> getCategoryAttributesByCategoryId(Long categoryId) {
    Set<CategoryAttribute> categoryAttributes = repository.findByCategory_Id(categoryId)
            .orElseThrow(() -> new CategoryAttributeNotFoundException(categoryId));
    return categoryAttributes.stream()
            .map(mapper::toDto)
            .collect(Collectors.toSet());
  }

  @Override
  public CategoryAttributeDto updateCategoryAttribute(
      Long categoryId, Long attributeId, UpdateCategoryAttributeDto categoryDto) {
    CategoryAttribute existingCategoryAttribute =
            repository
                    .findByCategory_IdAndAttribute_Id(categoryId, attributeId)
                    .orElseThrow(() -> new CategoryAttributeNotFoundException(categoryId, attributeId));
    mapper.updateEntityFromDto(existingCategoryAttribute,categoryDto);
    CategoryAttribute updatedCategoryAttribute = repository.save(existingCategoryAttribute);
    return mapper.toDto(updatedCategoryAttribute);
  }
}
