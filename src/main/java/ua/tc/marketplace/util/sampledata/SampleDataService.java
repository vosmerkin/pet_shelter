package ua.tc.marketplace.util.sampledata;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.tc.marketplace.exception.category.CategoryAttributeNotFoundException;
import ua.tc.marketplace.model.entity.*;
import ua.tc.marketplace.model.enums.UserRole;
import ua.tc.marketplace.model.enums.ValueType;
import ua.tc.marketplace.repository.*;
import ua.tc.marketplace.service.CategoryService;
import ua.tc.marketplace.service.UserService;

@RequiredArgsConstructor
@Service
@Slf4j
public class SampleDataService {

  private List<Long> userIds;
  private List<Long> categoryIds;
  private List<Attribute> attributes;
  private final UserRepository userRepository;
  private final CategoryRepository categoryRepository;
  private final AttributeRepository attributeRepository;
  private final CategoryAttributeRepository categoryAttributeRepository;
  private final AdRepository adRepository;
  private final UserService userService;
  private final CategoryService categoryService;
  private final Faker faker = new Faker(new Locale("en"));

  //  @PostConstruct
  //  public void generateTestData() {
  //
  //    addSampleData();
  //  }

  private void setUserIds() {
    log.info("setting userIds list");
    if (userIds == null || userIds.isEmpty()) {
      userIds = userRepository.findAll().stream().map(User::getId).toList();
    }
    if (userIds.isEmpty()) {
      addSampleUsers(10);
    }
  }

  private void setCategoryIds() {
    log.info("setting categoryIds list");
    if (categoryIds == null || categoryIds.isEmpty()) {
      categoryIds = categoryRepository.findAll().stream().map(Category::getId).toList();
    }
    if (categoryIds.isEmpty()) {
      addSampleCategories();
      categoryIds = categoryRepository.findAll().stream().map(Category::getId).toList();
    }
  }

  private void setAttributes() {
    log.info("setting attributes list");
    if (attributes == null || attributes.isEmpty()) {
      attributes = attributeRepository.findAll();
    }
    if (attributes.isEmpty()) {
      addSampleAttributes();
      attributes = attributeRepository.findAll();
    }
  }

  public void addSampleData(int count) {
    addSampleUsers(count);
    //    addSampleAttributes();
    //    addSampleCategories();
    addCategoryAttributeOptions();
    addSampleAds(count);
  }

  public void addSampleUsers(int count) {
    for (int i = 0; i < count; i++) {
      User user =
          User.builder()
              .email(faker.internet().emailAddress())
              .password(faker.internet().password(8, 16)) // generate dummy password
              .userRole(faker.options().option(UserRole.class))
              .firstName(faker.name().firstName())
              .lastName(faker.name().lastName())
              .enabled(true)
              // Optional fields like profilePicture, contactInfo, location can be added if needed
              .build();

      userRepository.save(user);
    }
    log.info("✅ Added {}  sample users to the database.", count);
  }

  private void addSampleCategories() {
    setAttributes();
    categoryRepository.save(new Category(null, "dog", attributes));
    categoryRepository.save(new Category(null, "cat", attributes));
    categoryRepository.save(new Category(null, "rodent", attributes));
    categoryRepository.save(new Category(null, "bird", attributes));
    categoryRepository.save(new Category(null, "fish", attributes));
    categoryRepository.save(new Category(null, "other", attributes));
    log.info("✅ Added sample categories to the database.");
  }

  private void addSampleAttributes() {
    attributeRepository.save(new Attribute(null, "breed", ValueType.STRING));
    attributeRepository.save(new Attribute(null, "age", ValueType.STRING));
    attributeRepository.save(new Attribute(null, "size", ValueType.STRING));
    attributeRepository.save(new Attribute(null, "gender", ValueType.STRING));
    attributeRepository.save(new Attribute(null, "coat length", ValueType.STRING));
    attributeRepository.save(new Attribute(null, "color", ValueType.STRING));
    attributeRepository.save(new Attribute(null, "health condition", ValueType.STRING));
    attributeRepository.save(new Attribute(null, "pet name", ValueType.STRING));
    log.info("✅ Added sample attributes to the database.");
  }

  private void addCategoryAttributeOptions() {
    List<Category> categories = categoryRepository.findAll();
    List<Attribute> attributes = attributeRepository.findAll();
    List<CategoryAttributeByNames> categoryAttributesByNames = getFromJson();
    List<CategoryAttribute> categoryAttributes =
        categoryAttributesByNames.stream()
            .map(
                byName ->
                    findByNames(
                        byName.getCategoryName(),
                        byName.getAttributeName(),
                        categories,
                        attributes,
                        byName.uaOptions)
                //                    new CategoryAttribute(
                //                        null,
                //                        findByName(categories, byName.categoryName),
                //                        findByName(attributes, byName.attributeName),
                //                        new HashSet<String>(byName.uaOptions))
                )
            .toList();
    //    for (CategoryAttribute categoryAttribute : categoryAttributes) {
    //      if (categoryAttribute.getAttribute() == null)
    //        System.out.println(categoryAttributes.indexOf(categoryAttribute));
    //    }
    categoryAttributeRepository.saveAll(categoryAttributes);
    //    for (CategoryAttribute categoryAttribute: categoryAttributes){
    //      categoryAttribute = categoryAttributeRepository.save
    //    }
  }

  private CategoryAttribute findByNames(
      String categoryName,
      String attributeName,
      List<Category> categories,
      List<Attribute> attributes,
      List<String> options) {
    Category category = findByName(categories, categoryName);
    Attribute attribute = findByName(attributes, attributeName);
    Long catId = category != null ? category.getId() : null;
    Long attrId = attribute != null ? attribute.getId() : null;

    CategoryAttribute categoryAttribute =
        categoryAttributeRepository.findByCategory_IdAndAttribute_Id(catId, attrId).orElse(null);
    if (categoryAttribute != null) {
      categoryAttribute.setValues(new HashSet<String>(options));
    }
    return categoryAttribute;
  }

  private <K> K findByName(List<K> list, String name) {
    if (list == null || name == null) {
      System.out.println("NULL!!!!!!!!!!!!!");
      return null;
    }
    for (K item : list) {
      if (item != null && name.equals(extractName(item))) {
        if (item == null) System.out.println("NULL!!!!!!!!!!!!!");
        return item;
      }
    }
    System.out.println("NULL!!!!!!!!!!!!!");
    return null;
  }

  private String extractName(Object item) {
    if (item instanceof Category category) {
      return category.getName();
    }
    if (item instanceof Attribute attribute) {
      return attribute.getName();
    }
    return null;
  }

  public void addSampleAds(int count) {
    setUserIds();
    setCategoryIds();

    for (int i = 0; i < count; i++) {
      Ad ad =
          Ad.builder()
              .author(userService.findUserById(faker.options().nextElement(userIds)))
              .title(faker.animal().name())
              .description(faker.lorem().sentence())
              .price(BigDecimal.valueOf(faker.random().nextDouble()))
              .category(categoryService.findCategoryById(faker.options().nextElement(categoryIds)))
              .isHot(faker.random().nextBoolean())
              .build();

      List<AdAttribute> adAttributes =
          ad.getCategory().getAttributes().stream()
              .map(
                  categoryAttribute ->
                      new AdAttribute(
                          null,
                          ad,
                          categoryAttribute,
                          generateAdAttribute(ad.getCategory(), categoryAttribute)))
              .toList();
      ad.setAdAttributes(adAttributes);

      adRepository.save(ad);
    }

    log.info("✅ Added {}  sample ads to the database.", count);
  }

  private String generateAdAttribute(Category category, Attribute attribute) {
    // get AdAttribute from one of possible values, to be implemented later
    List<String> options = null;
    CategoryAttribute categoryAttribute =
        categoryAttributeRepository
            .findByCategory_IdAndAttribute_Id(category.getId(), attribute.getId())
            .orElse(null);
    if (categoryAttribute != null) {
      options = new ArrayList<>(categoryAttribute.getValues());
    }
    String atrValue = faker.options().nextElement(options);
    return atrValue;
  }

  private List<CategoryAttributeByNames> getFromJson() {
    ObjectMapper mapper = new ObjectMapper();
    List<CategoryAttributeByNames> categoryAttributes = new ArrayList<>();
    //    File file = new File("/src/main/resources/category_attribute_options.json");
    //    System.out.println(file.getAbsolutePath());
    //    try {
    //      // Read JSON file and convert to Java object
    //      categoryAttributes =
    //          mapper.readValue(
    //              new File("/src/main/resources/category_attribute_options.json"),
    //              new TypeReference<List<CategoryAttributeByNames>>() {});
    //    } catch (Exception e) {
    //      e.printStackTrace();
    //    }

    try (InputStream inputStream =
        getClass().getClassLoader().getResourceAsStream("category_attribute_options.json")) {

      if (inputStream == null) {
        throw new IllegalArgumentException(
            "File not found! Make sure category_attribute_options.json");
      }

      categoryAttributes =
          mapper.readValue(inputStream, new TypeReference<List<CategoryAttributeByNames>>() {});
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return categoryAttributes;
  }

  @Data
  private static class CategoryAttributeByNames {
    private String attributeName;
    private String categoryName;
    private List<String> uaOptions;
    private List<String> enOptions;
  }
}
