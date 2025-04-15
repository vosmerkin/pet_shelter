package ua.tc.marketplace.controller;

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
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController implements UserOpenApi {

  private final UserService userService;


  @Override
  @GetMapping("/all")
  public ResponseEntity<Page<UserDto>> getAllUsers(@PageableDefault Pageable pageable) {
    log.info("Get all users request: {}" , pageable);
    return ResponseEntity.status(HttpStatus.OK).body(userService.findAll(pageable));
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
    log.info("Get users by id request: id={}" , id);
    return ResponseEntity.status(HttpStatus.OK).body(userService.findUserDtoById(id));
  }

  @Override
  @PutMapping()
  public ResponseEntity<UserDto> updateUser(@RequestBody @Valid UpdateUserDto userDto) {
    log.info("Update user request: {}" , userDto);
    return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userDto));
  }

  @Override
  @PreAuthorize("@SecurityService.hasAnyRoleAndOwnership(#id)")
//  @PreAuthorize("@securityService.hasAnyRoleAndOwnership(#id)")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    log.info("Delete user request: id={}" , id);
    userService.deleteUserById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
