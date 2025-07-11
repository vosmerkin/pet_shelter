package ua.tc.marketplace.service.impl;

import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.tc.marketplace.exception.auth.EmailAlreadyRegisteredException;
import ua.tc.marketplace.exception.user.UserNotFoundException;
import ua.tc.marketplace.model.dto.user.CreateUserDto;
import ua.tc.marketplace.model.dto.user.UpdateUserDto;
import ua.tc.marketplace.model.dto.user.UserDto;
import ua.tc.marketplace.model.entity.Photo;
import ua.tc.marketplace.model.entity.User;
import ua.tc.marketplace.repository.UserRepository;
import ua.tc.marketplace.service.PhotoStorageService;
import ua.tc.marketplace.service.UserService;
import ua.tc.marketplace.util.mapper.UserMapper;

/**
 * Implementation of the {@link UserService} interface. Provides methods for creating, retrieving,
 * updating, and deleting users.
 */
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final PhotoStorageService photoService;

  /**
   * Retrieves a paginated list of all users.
   *
   * @param pageable Pagination information (page number, size, sorting).
   * @return A page of UserDto objects.
   */
  @Override
  public Page<UserDto> findAll(Pageable pageable) {
    Page<User> users = userRepository.findAll(pageable);
    return users.stream()
        .collect(Collectors.collectingAndThen(Collectors.toList(), PageImpl::new))
        .map(userMapper::toDto);
  }

  /**
   * Retrieves a user by their ID.
   *
   * @param id The ID of the user to retrieve.
   * @return The UserDto representing the found user.
   * @throws UserNotFoundException If the user is not found.
   */
  @Override
  public UserDto findUserDtoById(Long id) {
    return userMapper.toDto(findUserById(id));
  }

  /**
   * Retrieves a user by their ID.
   *
   * @param id The ID of the user to retrieve.
   * @return The User representing the found user.
   * @throws UserNotFoundException If the user is not found.
   */
  @Override
  public User findUserById(Long id) {
    return getUser(id);
  }

  /**
   * Retrieves a user by their ID.
   *
   * @param email The email of the user to retrieve.
   * @return The User representing the found user.
   * @throws UserNotFoundException If the user is not found.
   */
  @Override
  public User findUserByEmail(String email) {
    return getUser(email);
  }

  /**
   * Retrieves a user by their ID.
   *
   * @param email The email of the user to retrieve.
   * @return The UserDto representing the found user.
   * @throws UserNotFoundException If the user is not found.
   */
  @Override
  public UserDto findUserDtoByEmail(String email) {
    return userMapper.toDto(findUserByEmail(email));
  }

  /**
   * Updates an existing user.
   *
   * @param updateUserDto The DTO containing updated user information.
   * @return The updated UserDto.
   * @throws UserNotFoundException If the user to update is not found.
   */
  @Transactional
  @Override
  public UserDto updateUser(@NonNull UpdateUserDto updateUserDto) {
    User existingUser = getUser(updateUserDto.id());
    userMapper.updateEntityFromDto(existingUser, updateUserDto);
    return userMapper.toDto(userRepository.save(existingUser));
  }

  /**
   * Deletes a user by their ID.
   *
   * @param id The ID of the user to delete.
   * @throws UserNotFoundException If the user to delete is not found.
   */
  @Transactional
  @Override
  public void deleteUserById(Long id) {
    User existingUser = getUser(id);
    userRepository.deleteById(existingUser.getId());
  }

  /**
   * Creates a new user.
   *
   * @param createUserDto The DTO containing user information for creation.
   * @return The created UserDto.
   */
  @Transactional
  @Override
  public UserDto createUser(CreateUserDto createUserDto) {
    if (UserExistsByEmail(createUserDto.email()))
      throw new EmailAlreadyRegisteredException(createUserDto.email());
    User user = userMapper.toEntity(createUserDto);
    user.setPassword(passwordEncoder.encode(createUserDto.password()));
    user = userRepository.save(user);
    if (createUserDto.profilePicture() != null) {
      Photo photo = photoService.saveUserPhoto(user.getId(), createUserDto.profilePicture());
      user.setProfilePicture(photo);
      user = userRepository.save(user);
    }
    //        user = getUser(user.getId());

    return userMapper.toDto(user);
  }

  //    @Override
  //    public UserDto createUser(UnverifiedUser unverifiedUser) {
  //        User user = userMapper.toEntity(unverifiedUser);
  //        user.setUserRole(UserRole.USER);
  //        return userMapper.toDto(userRepository.save(user));
  //    }

  /**
   * Retrieves a user by their ID, throwing a UserNotFoundException if not found.
   *
   * @param id The ID of the user to retrieve.
   * @return The found User entity.
   * @throws UserNotFoundException If the user is not found.
   */
  private User getUser(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
  }

  /**
   * Retrieves a user by their email, throwing a UserNotFoundException if not found.
   *
   * @param email The email of the user to retrieve.
   * @return The found User entity.
   * @throws UserNotFoundException If the user is not found.
   */
  private User getUser(String email) {
    return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
  }

  /**
   * Returns true if a user with the specified email exists, otherwise returns false.
   *
   * @param email The email of the user to check.
   * @return {@code true} if the user exists, {@code false} otherwise.
   */
  public Boolean UserExistsByEmail(String email) {
    return userRepository.findByEmail(email).isPresent();
  }
}
