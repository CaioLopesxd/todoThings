package api.main.controllers;


import api.main.dtos.auth.RegisterRequestDto;
import api.main.mappers.auth.UserMapper;
import api.main.models.User;
import api.main.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
class AuthController {
    private AuthService authService;
    private UserMapper userMapper;

    AuthController(AuthService _authService) {
        this.authService = _authService;
        this.userMapper = new UserMapper();
    }

    @PostMapping
    public ResponseEntity<User> post(@RequestBody RegisterRequestDto requestDto) {
        User user = userMapper.toUser(requestDto);
        return ResponseEntity.ok(authService.createUser(user));
    }

}
