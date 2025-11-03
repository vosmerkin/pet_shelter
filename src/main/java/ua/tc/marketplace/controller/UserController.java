package ua.tc.marketplace.controller;

import static ua.tc.marketplace.config.ApiURLs.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.tc.marketplace.model.dto.user.UpdateUserDto;
import ua.tc.marketplace.model.dto.user.UserDto;
import ua.tc.marketplace.service.UserService;
import ua.tc.marketplace.util.openapi.UserOpenApi;

@RequiredArgsConstructor
@RestController
@RequestMapping(USER_BASE)
@Slf4j
public class UserController implements UserOpenApi {

  private final UserService userService;

  @Override
  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping(USER_GET_ALL)
  public ResponseEntity<Page<UserDto>> getAllUsers(@PageableDefault Pageable pageable) {
    log.info("Get all users request: {}", pageable);
    return ResponseEntity.status(HttpStatus.OK).body(userService.findAll(pageable));
  }

  @Override
  @GetMapping(USER_GET_BY_ID)
  public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
    log.info("Get user by id request: id={}", id);
    return ResponseEntity.status(HttpStatus.OK).body(userService.findUserDtoById(id));
  }

  @GetMapping(USER_GET_BY_EMAIL)
  public ResponseEntity<Boolean> getUserById(@PathVariable String email) {
    log.info("Get user by email request: email={}", email);
    return ResponseEntity.status(HttpStatus.OK).body(userService.UserExistsByEmail(email));
  }

  @Override
  @PutMapping(USER_UPDATE)
  @PreAuthorize("@securityService.hasAnyRoleAndOwnership(#userDto.id)")
  public ResponseEntity<UserDto> updateUser(@RequestBody @Valid UpdateUserDto userDto) {
    log.info("Update user request: {}", userDto);
    return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userDto));
  }

  @Override
  @PreAuthorize("hasAuthority('ADMIN') or @securityService.hasAnyRoleAndOwnership(#id)")
  @DeleteMapping(USER_DELETE)
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    log.info("Delete user request: id={}", id);
    userService.deleteUserById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
