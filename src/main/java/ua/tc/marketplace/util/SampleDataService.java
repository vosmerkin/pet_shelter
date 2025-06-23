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
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SampleDataService {

    private List<Long> userIds;
    private List<Long> categoryIds;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final AttributeRepository attributeRepository;
//    private final AdAttribute a;
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
        categoryRepository.save(new Category(null,"dog",null) );
        categoryRepository.save(new Category(null,"cat",null) );
        categoryRepository.save(new Category(null,"rodent",null) );
        categoryRepository.save(new Category(null,"bird",null) );
        categoryRepository.save(new Category(null,"fish",null) );
        categoryRepository.save(new Category(null,"other",null) );
    }
    private void addSampleAttributes(){
 attributeRepository.save(new Attribute( null,"breed" , ValueType.STRING));
        ('breed', 'STRING'),
        ('age', 'STRING'),
        ('size', 'STRING'),
        ('gender', 'STRING'),
        ('coat_length', 'STRING'),
        ('color', 'STRING'),
        ('health_condition', 'STRING'),
        ('pet_name', 'STRING');
    }

//    public void addSampleAds(int count){
//        setUserIds();
//        setCategoryIds();
//
//        List<AdAttribute> adAttributes =
//        Ad ad= Ad.builder()
//                .author(userService.findUserById(faker.options().nextElement(userIds)))
//                .title(faker.animal().name())
//                .description(faker.lorem().sentence())
//                .price(BigDecimal.valueOf(faker.random().nextDouble()))
//                .category(categoryService.findCategoryById(faker.options().nextElement(categoryIds)))
//                        .              build();
//    }
}
