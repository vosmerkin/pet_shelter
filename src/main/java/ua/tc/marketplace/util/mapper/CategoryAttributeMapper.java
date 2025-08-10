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

  //
  //    @Mapping(source = "attributes", target = "attribute")
  //    @Mapping(source = "entity", target = "adsCount", qualifiedByName = "countAllCategoryAds")
  //    CategoryCountedDto toCategoryCountedDto(Category entity);
  //
  //    @Mapping(source = "attribute", target = "attributes")
  //    Category toEntity(CategoryDto dto);
  //
  //    @Mapping(target = "id", ignore = true)
  //    @Mapping(source = "attributeIds", target = "attributes", qualifiedByName =
  // "idToAttributeList")
  //    Category toEntity(CreateCategoryDto dto);
  //
  //    @Mapping(source = "attributes", target = "attributeIds", qualifiedByName =
  // "attributeToIdList")
  //    CreateCategoryDto toCreateCategoryDto(Category entity);
  //
  //    @Mapping(target = "id", ignore = true)
  //    @Mapping(source = "name", target = "name")
  //    @Mapping(source = "attributeIds", target = "attributes", qualifiedByName =
  // "idToAttributeList")
  //    void updateEntityFromDto(@MappingTarget Category category, UpdateCategoryDto dto);
  //
  //    @Named("attributeToIdList")
  //    @IterableMapping(qualifiedByName = "attributeToId")
  //    List<Long> attributeToIdList(List<Attribute> attributes);
  //
  //    @Named("idToAttributeList")
  //    @IterableMapping(qualifiedByName = "idToAttribute")
  //    List<Attribute> idToAttributeList(List<Long> ids);
  //
  //    @Named("mapIdToCategory")
  //    default Category mapIdToCategory(Long categoryId, @Context CategoryService categoryService)
  // {
  //        return categoryId == null ? null : categoryService.findCategoryById(categoryId);
  //    }
}
