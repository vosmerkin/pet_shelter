package ua.tc.marketplace.util;

import com.github.javafaker.Faker;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.tc.marketplace.model.entity.*;
import ua.tc.marketplace.model.enums.UserRole;
import ua.tc.marketplace.model.enums.ValueType;
import ua.tc.marketplace.repository.AdRepository;
import ua.tc.marketplace.repository.AttributeRepository;
import ua.tc.marketplace.repository.CategoryRepository;
import ua.tc.marketplace.repository.UserRepository;
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
    if (categoryIds == null ||categoryIds.isEmpty()) {
      categoryIds = categoryRepository.findAll().stream().map(Category::getId).toList();
    }
    if (categoryIds.isEmpty()) {
      addSampleCategories();
    }
  }

  private void setAttributes() {
    log.info("setting attributes list");
    if (attributes == null|| attributes.isEmpty()) {
      attributes = attributeRepository.findAll();
    }
    if (attributes.isEmpty()) {
      addSampleAttributes();
    }
  }

  public void addSampleData(int count) {
    addSampleUsers(count);
    addSampleAttributes();
    addSampleCategories();
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
    attributeRepository.save(new Attribute(null, "coat_length", ValueType.STRING));
    attributeRepository.save(new Attribute(null, "color", ValueType.STRING));
    attributeRepository.save(new Attribute(null, "health_condition", ValueType.STRING));
    attributeRepository.save(new Attribute(null, "pet_name", ValueType.STRING));
    log.info("✅ Added sample attributes to the database.");
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

  String generateAdAttribute(Category category, Attribute attribute) {
    // get AdAttribute from one of possible values, to be implemented later
    //    faker.options().nextElement(getCategoryAttributeValues(Category category, Attribute
    // attribute));
    return "some_value";
  }
}
