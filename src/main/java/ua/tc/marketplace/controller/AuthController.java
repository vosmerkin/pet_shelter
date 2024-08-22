package ua.tc.marketplace.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.tc.marketplace.model.auth.AuthError;
import ua.tc.marketplace.model.auth.AuthRequest;
import ua.tc.marketplace.model.dto.user.CreateUserDto;
import ua.tc.marketplace.model.dto.user.UserDto;
import ua.tc.marketplace.service.UserService;
import ua.tc.marketplace.util.openapi.AuthOpenApi;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController  implements AuthOpenApi {

    private final UserService userService;

    @Override
    @PostMapping("/login")
    public ResponseEntity authenticate(@RequestBody AuthRequest authRequest) {
        try {
            return ResponseEntity.ok(userService.authentificate(authRequest));
        }catch (BadCredentialsException e){
            AuthError authErrorResponse = new AuthError(HttpStatus.BAD_REQUEST,"Invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(authErrorResponse);
        }catch (Exception e){
            AuthError authErrorResponse = new AuthError(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(authErrorResponse);
        }
    }

    @Override
    @PostMapping("/signup")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserDto userDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.createUser(userDto));
    }


}
