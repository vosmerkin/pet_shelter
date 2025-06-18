package ua.tc.marketplace.util;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.tc.marketplace.model.entity.Ad;
import ua.tc.marketplace.model.entity.AdAttribute;
import ua.tc.marketplace.model.entity.Category;
import ua.tc.marketplace.model.entity.User;
import ua.tc.marketplace.model.enums.UserRole;
import ua.tc.marketplace.repository.CategoryRepository;
import ua.tc.marketplace.repository.UserRepository;
import ua.tc.marketplace.service.CategoryService;
import ua.tc.marketplace.service.UserService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SampleDataService {

    private List<Long> userIds;
    private List<Long> categoryIds;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final AdAttribute categoryRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final Faker faker = new Faker(new Locale("en"));

    private  void setUserIds(){
        if (userIds==null){
            userIds = userRepository.findAll().stream()
                    .map(User::getId)
                    .toList();
        }
    }

    private void setCategoryIds(){
        if (categoryIds==null){
            categoryIds=categoryRepository.findAll().stream()
                    .map(Category::getId)
                    .toList();
        }
    }


    public void addSampleUsers(int count) {
        for (int i = 0; i < count; i++) {
            User user = User.builder()
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

    private void addSampleCategories(){
        categoryRepository.save(new(Category("dog")));
        INSERT INTO category (name)
        VALUES ('dog'),
                ('cat'),
                ('rodent'),
                ('bird'),
                ('fish'),
                ('other');
    }

    public void addSampleAds(int count){
        setUserIds();
        setCategoryIds();

        List<AdAttribute> adAttributes =
        Ad ad= Ad.builder()
                .author(userService.findUserById(faker.options().nextElement(userIds)))
                .title(faker.animal().name())
                .description(faker.lorem().sentence())
                .price(BigDecimal.valueOf(faker.random().nextDouble()))
                .category(categoryService.findCategoryById(faker.options().nextElement(categoryIds)))
                        .              build();
    }
}
