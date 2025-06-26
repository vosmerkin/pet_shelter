package ua.tc.marketplace.util;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.tc.marketplace.model.entity.*;
import ua.tc.marketplace.model.enums.UserRole;
import ua.tc.marketplace.model.enums.ValueType;
import ua.tc.marketplace.repository.AttributeRepository;
import ua.tc.marketplace.repository.CategoryRepository;
import ua.tc.marketplace.repository.UserRepository;
import ua.tc.marketplace.service.CategoryService;
import ua.tc.marketplace.service.UserService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class SampleDataService {

  private List<Long> userIds;
  private List<Long> categoryIds;
  private List<Attribute> attributes;
  private final UserRepository userRepository;
  private final CategoryRepository categoryRepository;
  private final AttributeRepository attributeRepository;
  //    private final AdAttribute a;
  private final UserService userService;
  private final CategoryService categoryService;
  private final Faker faker = new Faker(new Locale("en"));

  private void setUserIds() {
    if (userIds == null) {
      userIds = userRepository.findAll().stream().map(User::getId).toList();
    }
  }

  private void setCategoryIds() {
    if (categoryIds == null) {
      categoryIds = categoryRepository.findAll().stream().map(Category::getId).toList();
    }
  }

  private void setAttributeIds() {
    if (attributes == null) {
      attributes = attributeRepository.findAll();
    }
  }

  public void addSampleData() {
    addSampleUsers(100);
    addSampleAttributes();
    addSampleCategories();

    addSampleAds(1000);
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
    System.out.println("✅ Added " + count + " sample users to the database.");
  }

  private void addSampleCategories() {
    setAttributeIds();
    categoryRepository.save(new Category(null, "dog", attributes));
    categoryRepository.save(new Category(null, "cat", attributes));
    categoryRepository.save(new Category(null, "rodent", attributes));
    categoryRepository.save(new Category(null, "bird", attributes));
    categoryRepository.save(new Category(null, "fish", attributes));
    categoryRepository.save(new Category(null, "other", attributes));
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
  }

  public void addSampleAds(int count) {
    setUserIds();
    setCategoryIds();

    //          List<AdAttribute> adAttributes =
    Ad ad =
        Ad.builder()
            .author(userService.findUserById(faker.options().nextElement(userIds)))
            .title(faker.animal().name())
            .description(faker.lorem().sentence())
            .price(BigDecimal.valueOf(faker.random().nextDouble()))
            .category(categoryService.findCategoryById(faker.options().nextElement(categoryIds)))
            .build();
      //          List<AdAttribute> adAttributes=
      AdAttribute adAttribute= new AdAttribute(null,ad,ad.getCategory().getAttributes().get(), fakerValue);
  }
}
