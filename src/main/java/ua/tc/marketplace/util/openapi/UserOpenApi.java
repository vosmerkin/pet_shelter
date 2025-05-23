package ua.tc.marketplace.util.openapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.tc.marketplace.controller.UserController;
import ua.tc.marketplace.model.dto.user.UpdateUserDto;
import ua.tc.marketplace.model.dto.user.UserDto;

/**
 * This interface defines the OpenAPI annotations for the {@link UserController} class. It provides endpoints
 * for managing users.
 */
@Tag(name = "User API", description = "API for managing users")
public interface UserOpenApi {

  @Operation(
      summary = "Get all users",
      description = "Retrieves a pageable list of all users.")
  @GetMapping("/all")
  ResponseEntity<Page<UserDto>> getAllUsers(@PageableDefault Pageable pageable);

  @Operation(
      summary = "Get user by ID",
      description = "Retrieves a user by its unique identifier.")
  @GetMapping("/{id}")
  ResponseEntity<UserDto> getUserById(@PathVariable Long id);

  @Operation(
          summary = "Verifies if user by email exists",
          description = "Returns true if a user by its unique email address is registered.")
  @GetMapping("/email/{email}")
  ResponseEntity<Boolean> getUserById(@PathVariable String email);

  @Operation(
      summary = "Updates an existing user",
      description = "Updates an existing user with the provided data.")
  @PutMapping()
  ResponseEntity<UserDto> updateUser(@RequestBody @Valid UpdateUserDto userDto);

  @Operation(
      summary = "Delete a user",
      description = "Deletes a user by its unique identifier.")
  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteUser(@PathVariable Long id);
}
