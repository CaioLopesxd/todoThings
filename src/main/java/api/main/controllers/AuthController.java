package api.main.controllers;

import api.main.dtos.auth.AuthResponseDto;
import api.main.dtos.auth.LoginRequestDto;
import api.main.dtos.auth.RegisterRequestDto;
import api.main.dtos.auth.UserDto;
import api.main.mappers.auth.UserMapper;
import api.main.models.User;
import api.main.service.AuthService;
import api.main.util.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
class AuthController {
    private final AuthService authService;
    private final UserMapper userMapper;

    AuthController(AuthService authService) {
        this.authService = authService;
        this.userMapper = new UserMapper();
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody RegisterRequestDto requestDto) {
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

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto requestDto) {
        Optional<User> authenticatedUser = authService.authenticateUser(requestDto.email(), requestDto.password());
        
        if (authenticatedUser.isPresent()) {
            User user = authenticatedUser.get();
            String token = authService.generateTokenForUser(user);
            UserDto userDto = userMapper.toUserDto(user);
            AuthResponseDto response = new AuthResponseDto(token, userDto);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        Optional<User> currentUser = SecurityUtils.getCurrentUser();
        
        if (currentUser.isPresent()) {
            UserDto userDto = userMapper.toUserDto(currentUser.get());
            return ResponseEntity.ok(userDto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().build();
    }
}
