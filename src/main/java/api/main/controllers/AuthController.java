package api.main.controllers;

import api.main.dtos.auth.LoginRequestDto;
import api.main.dtos.auth.RegisterRequestDto;
import api.main.dtos.auth.UserDto;
import api.main.mappers.auth.UserMapper;
import api.main.models.User;
import api.main.service.AuthService;
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
    public ResponseEntity<UserDto> register(@RequestBody RegisterRequestDto requestDto) {
        try {
            User user = userMapper.toUser(requestDto);
            User savedUser = authService.createUser(user);
            UserDto userDto = userMapper.toUserDto(savedUser);
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto requestDto) {
        Optional<User> authenticatedUser = authService.authenticateUser(requestDto.email(), requestDto.password());
        
        if (authenticatedUser.isPresent()) {
            UserDto userDto = userMapper.toUserDto(authenticatedUser.get());
            return ResponseEntity.ok(userDto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
