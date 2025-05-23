package ua.tc.marketplace.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.tc.marketplace.model.dto.user.CreateUserDto;
import ua.tc.marketplace.model.dto.user.UpdateUserDto;
import ua.tc.marketplace.model.dto.user.UserDto;
import ua.tc.marketplace.model.entity.User;

public interface UserService {

  Page<UserDto> findAll(Pageable pageable);

  UserDto createUser(CreateUserDto createUserDto);

  UserDto findUserDtoById(Long id);

  User findUserById(Long id);

  UserDto findUserDtoByEmail(String email);

  User findUserByEmail(String email);

  UserDto updateUser(UpdateUserDto updateUserDto);

  void deleteUserById(Long id);

  Boolean UserExistsByEmail(String email);
}
