package api.main.controllers;

import api.main.dtos.auth.*;
import api.main.mappers.auth.UserMapper;
import api.main.models.User;
import api.main.service.AuthService;
import api.main.util.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;
    private final UserMapper userMapper;

    AuthController(AuthService authService) {
        this.authService = authService;
        this.userMapper = new UserMapper();
    }

    @PostMapping("/auth/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody @Valid RegisterRequestDto requestDto) {
        try {
            User user = userMapper.toUser(requestDto);
            User savedUser = authService.createUser(user);
            String token = authService.generateTokenForUser(savedUser);
            UserDto userDto = userMapper.toUserDto(savedUser);
            AuthResponseDto response = new AuthResponseDto(token, userDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid LoginRequestDto requestDto) {
        User user = authService.authenticateUser(requestDto.email(), requestDto.password());
        String token = authService.generateTokenForUser(user);
        UserDto userDto = userMapper.toUserDto(user);
        AuthResponseDto response = new AuthResponseDto(token, userDto);
        return ResponseEntity.ok(response);

    }
    
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        User current = authService.getAuthenticatedUser(SecurityUtils.getCurrentUser().getId());
        return ResponseEntity.ok(userMapper.toUserDto(current));

    }
    @PatchMapping("/me")
    public ResponseEntity<UserDto> updateUser(@RequestBody @Valid UpdateUserDto updateUserDto) {
        return ResponseEntity.ok(authService.updateUser(updateUserDto, SecurityUtils.getCurrentUser()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/contact")
    public ResponseEntity<User> addNewContact(@RequestBody @Valid NewContactDto newContactDto) {
        User ownerContact = authService.addContact(SecurityUtils.getCurrentUser(), newContactDto);
        return ResponseEntity.ok(ownerContact.getContacts().get(ownerContact.getContacts().size()-1));
    }
}
