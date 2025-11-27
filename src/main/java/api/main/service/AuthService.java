package api.main.service;

import api.main.exceptions.auth.EmailOrPasswordError;
import api.main.models.User;
import api.main.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(EmailOrPasswordError::new);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new EmailOrPasswordError();
        }

        return user;
    }


    public String generateTokenForUser(User user) {
        return jwtService.generateToken(user.getId());
    }
}
