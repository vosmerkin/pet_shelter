package ua.tc.marketplace.util.mapper;

import java.util.Collections;
import java.util.List;

import org.mapstruct.*;
import ua.tc.marketplace.config.MapperConfig;
import ua.tc.marketplace.model.dto.category.CategoryCountedDto;
import ua.tc.marketplace.model.dto.category.CategoryDto;
import ua.tc.marketplace.model.dto.category.CreateCategoryDto;
import ua.tc.marketplace.model.dto.category.UpdateCategoryDto;
import ua.tc.marketplace.model.entity.Attribute;
import ua.tc.marketplace.model.entity.Category;
import ua.tc.marketplace.service.CategoryService;

@Mapper(config = MapperConfig.class,
        uses = {AttributeMapper.class, CategoryCountResolver.class, CategoryService.class},
        imports = {Collections.class})
public interface CategoryMapper {

    @Mapping(source = "attributes", target = "attribute")
    CategoryDto toCategoryDto(Category entity);

    @Mapping(source = "attributes", target = "attribute")
    @Mapping(source = "entity", target = "adsCount", qualifiedByName = "countAllCategoryAds")
    CategoryCountedDto toCategoryCountedDto(Category entity);

    @Mapping(source = "attribute", target = "attributes")
    Category toEntity(CategoryDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "attributeIds", target = "attributes", qualifiedByName = "idToAttributeList")
    Category toEntity(CreateCategoryDto dto);

    @Mapping(source = "attributes", target = "attributeIds", qualifiedByName = "attributeToIdList")
    CreateCategoryDto toCreateCategoryDto(Category entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "attributeIds", target = "attributes", qualifiedByName = "idToAttributeList")
    void updateEntityFromDto(@MappingTarget Category category, UpdateCategoryDto dto);

    @Named("attributeToIdList")
    @IterableMapping(qualifiedByName = "attributeToId")
    List<Long> attributeToIdList(List<Attribute> attributes);

    @Named("idToAttributeList")
    @IterableMapping(qualifiedByName = "idToAttribute")
    List<Attribute> idToAttributeList(List<Long> ids);

    @Named("mapIdToCategory")
    default Category mapIdToCategory(Long categoryId, @Context CategoryService categoryService) {
        return categoryId == null ? null : categoryService.findCategoryById(categoryId);
    }
}
