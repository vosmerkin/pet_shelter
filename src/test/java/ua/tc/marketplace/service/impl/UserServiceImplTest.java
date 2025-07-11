package ua.tc.marketplace.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.tc.marketplace.exception.user.UserNotFoundException;
import ua.tc.marketplace.model.dto.user.CreateUserDto;
import ua.tc.marketplace.model.dto.user.UpdateUserDto;
import ua.tc.marketplace.model.dto.user.UserDto;
import ua.tc.marketplace.model.entity.User;
import ua.tc.marketplace.model.enums.UserRole;
import ua.tc.marketplace.repository.UserRepository;
import ua.tc.marketplace.util.mapper.UserMapper;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto userDto;
    private UserDto userDtoAfterUpdate;
    private User user;
    private User userAfterUpdate;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(1L)
                .email("taras@shevchenko.ua")
                .password("password")
                .userRole(UserRole.USER)
                .firstName("Taras")
//                .lastName("Shevchenko")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        userAfterUpdate = User.builder()
                .id(1L)
                .email("taras@shevchenko.ua")
                .password("password")
                .userRole(UserRole.USER)
                .firstName("Taras")
                .lastName("Shevchenko")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userDto = UserDto.builder()
                .id(1L)
                .email("taras@shevchenko.ua")
                .password("password")
                .userRole(UserRole.USER.toString())
                .firstName("Taras")
//                .lastName("Shevchenko")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        userDtoAfterUpdate = UserDto.builder()
                .id(1L)
                .email("taras@shevchenko.ua")
                .password("password")
                .userRole(UserRole.USER.toString())
                .firstName("Taras")
                .lastName("Shevchenko")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void findUserById_shouldFindUser_Dto_whenExists() {
        // Arrange
        Long userId = 1L;
        User user = new User();


        // Mock repository
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Mock mapper
        when(userMapper.toDto(user)).thenReturn(userDto);

        // run method
        UserDto result = userService.findUserDtoById(userId);

        // Assert results
        assertEquals(userDto, result);

        // Verify that repository method was called with correct argument
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void findUserDtoById_shouldThrow_whenNotExists() {
        // Arrange
        Long userId = 1L;

        // Mock repository method to return an empty Optional (simulating not found scenario)
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        // Use assertThrows to verify that AdNotFoundException is thrown
        assertThrows(UserNotFoundException.class, () -> userService.findUserDtoById(userId));

        // Verify that repository method was called with correct argument
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void createUser_shouldCreate_whenValidInput() {
        // createUserDto, received from controller
        CreateUserDto createUserDto =
                new CreateUserDto("taras@shevchenko.ua", "password", "USER", "Taras", null, null, null);

        // User entity after mapping from createUserDto
//        User user =
//                new User(
//                        1L,
//                        "taras@shevchenko.ua",
//                        "password",
//                        UserRole.USER,
//                        "Taras",
//                        null,
//                        null,
//                        null,
//                        Collections.emptyList(),
//                        Collections.emptyList(),
//                        Collections.emptyList(),
//                        null,
//                        null,
//                        null);
        // User Dto after mapping from saved entity
//        UserDto userDto =
//                new UserDto(
//                        1L,
//                        "taras@shevchenko.ua",
//                        "password",
//                        "USER",
//                        "Taras",
//                        null,
//                        null,
//                        null,
//                        Collections.emptyList(),
//                        LocalDateTime.now(),
//                        null);

        // Mock mapper from createUserDto to entity
        when(userMapper.toEntity(createUserDto)).thenReturn(user);

        // Mock password encoding
        when(passwordEncoder.encode(createUserDto.password())).thenReturn("encoded_password");

        // Mock repository
        when(userRepository.save(user)).thenReturn(user);

        // Mock mapper from entity back to Dto
        when(userMapper.toDto(user)).thenReturn(userDto);

        // Act
        UserDto result = userService.createUser(createUserDto);

        // Assert
        assertEquals(userDto, result);

        // Verify that userMapper method was called with correct argument
        verify(userMapper, times(1)).toEntity(createUserDto);

        // Verify that passwordEncoder.encode method was called with correct argument
        verify(passwordEncoder, times(1)).encode(ArgumentMatchers.anyString());

        // Verify that userRepository method was called with correct argument
        verify(userRepository, times(1)).save(user);

        // Verify that userMapper method was called with correct argument
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void updateUser_shouldUpdate_whenValidInput() {
        // Arrange

        // dto with updated info
        UpdateUserDto updateUserDto =
                new UpdateUserDto(
                        1L,
                        "taras@shevchenko.ua",
                        "password",
                        "USER",
                        "Taras",
                        "Shevchenko",
                        null,
                        Collections.emptyList());

        // entity of existing user, found in database
        User existingUser = user;
//                new User(
//                        1L,
//                        "taras@shevchenko.ua",
//                        "password",
//                        UserRole.USER,
//                        "Taras",
//                        null,
//                        null,
//                        null,
//                        Collections.emptyList(),
//                        Collections.emptyList(),
//                        Collections.emptyList(),
//                        null,
//                        null,
//                        null);

        // entity of updated user
        User updatedUser = userAfterUpdate;
//                new User(
//                        1L,
//                        "taras@shevchenko.ua",
//                        "password",
//                        UserRole.USER,
//                        "Taras",
//                        "Shevchenko",
//                        null,
//                        null,
//                        Collections.emptyList(),
//                        Collections.emptyList(),
//                        Collections.emptyList(),
//                        null,
//                        null,
//                        null);

        // User Dto after mapping from updated entity
        UserDto updatedUserDto = userDtoAfterUpdate;
//                new UserDto(
//                        1L,
//                        "taras@shevchenko.ua",
//                        "password",
//                        "USER",
//                        "Taras",
//                        "Shevchenko",
//                        null,
//                        null,
//                        Collections.emptyList(),
//                        LocalDateTime.now(),
//                        null);

        // Mock userRepository to return existingUser when findById is called
        when(userRepository.findById(updateUserDto.id())).thenReturn(Optional.of(existingUser));

        // Mock userMapper to return updatedUser when updateEntityFromDto is called
        doAnswer(
                invocation -> {
                    UpdateUserDto dto = invocation.getArgument(1);
                    User userToUpdate = invocation.getArgument(0);
                    userToUpdate.setEmail(dto.email());
                    userToUpdate.setUserRole(UserRole.valueOf(dto.userRole()));
                    userToUpdate.setFirstName(dto.firstName());
                    userToUpdate.setLastName(dto.lastName());
                    userToUpdate.setContactInfo(dto.contactInfo());
                    userToUpdate.setFavorites(dto.favorites()); // Setting the updated category
                    return null;
                })
                .when(userMapper)
                .updateEntityFromDto(existingUser, updateUserDto);

        // Mock repository
        when(userRepository.save(existingUser)).thenReturn(updatedUser);

        // Mock mapper
        when(userMapper.toDto(updatedUser)).thenReturn(updatedUserDto);

        // Act
        UserDto result = userService.updateUser(updateUserDto);

        // Assert
        assertEquals(updatedUserDto, result);

        // Verify that userRepository method was called with correct argument
        verify(userRepository, times(1)).findById(updatedUserDto.id());

        // Verify that adMapper method was called with correct argument
        verify(userMapper, times(1)).updateEntityFromDto(existingUser, updateUserDto);

        // Verify that userRepository method was called with correct argument
        verify(userRepository, times(1)).save(existingUser);

        // Verify that adMapper method was called with correct argument
        verify(userMapper, times(1)).toDto(updatedUser);
    }

    @Test
    void updateUser_shouldThrow_whenUserNotExists() {
        // Arrange
        // dto with updated info
        UpdateUserDto updateUserDto =
                new UpdateUserDto(
                        1L,
                        "taras@shevchenko.ua",
                        "password",
                        "USER",
                        "Taras",
                        "Shevchenko",
                        null,
                        Collections.emptyList());

        assertThrows(
                UserNotFoundException.class, () -> userService.findUserDtoById(updateUserDto.id()));

        // Verify that repository method was called with correct argument
        verify(userRepository, times(1)).findById(ArgumentMatchers.anyLong());
    }

    @Test
    void deleteUser_shouldDelete() {
        // Arrange
        Long userId = 1L;

        // entity of existing user, found in database
        User existingUser = user;
//                new User(
//                        1L,
//                        "taras@shevchenko.ua",
//                        "password",
//                        UserRole.USER,
//                        "Taras",
//                        null,
//                        null,
//                        null,
//                        Collections.emptyList(),
//                        Collections.emptyList(),
//                        Collections.emptyList(),
//                        null,
//                        null,
//                        null);

        // Mock userRepository to return existingUser when findById is called
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        // Act
        userService.deleteUserById(userId);

        // Verify that userRepository method was called with correct argument
        verify(userRepository, times(1)).findById(userId);

        // Verify that userRepository method was called with correct argument
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void deleteUser_shouldThrowException_whenUserNotExists() {
        // Arrange
        Long userId = 1L;

        // Mock userRepository to throw AdNotFoundException when findById is called
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(userId));

        // Verify that userRepository method was called with correct argument
        verify(userRepository, times(1)).findById(userId);

        // Verify that userRepository method was called with correct argument
        verify(userRepository, never()).deleteById(userId);
    }
}
