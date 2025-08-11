package ua.tc.marketplace.util.mapper;

import org.mapstruct.*;
import ua.tc.marketplace.config.MapperConfig;
import ua.tc.marketplace.model.dto.category.*;
import ua.tc.marketplace.model.entity.CategoryAttribute;
import ua.tc.marketplace.service.CategoryService;

@Mapper(
    config = MapperConfig.class,
    uses = {AttributeMapper.class, CategoryService.class})
public interface CategoryAttributeMapper {

  //    @Mapping(source = "attributes", target = "attribute")
  //  @Mapping(target = "id", ignore = true)
  @Mapping(target = "categoryId", source = "category.id")
  @Mapping(target = "attributeId", source = "attribute.id")
  CategoryAttributeDto toDto(CategoryAttribute entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "category", ignore = true)
  @Mapping(target = "attribute", ignore = true)
  void updateEntityFromDto(@MappingTarget CategoryAttribute entity, UpdateCategoryAttributeDto dto);
}
