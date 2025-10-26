package api.main.controllers;


import api.main.dtos.auth.RegisterRequestDto;
import api.main.models.User;
import api.main.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
class AuthController {
    private AuthService authService;

    AuthController(AuthService _authService) {
        this.authService = _authService;
    }

    @PostMapping
    public ResponseEntity<User> post(@RequestBody RegisterRequestDto requestDto) {
        User user = new User(requestDto.name(),  requestDto.email(), requestDto.password());
        authService.createUser(user);
        return ResponseEntity.ok(user);
    }

}
