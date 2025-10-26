package api.main.service;

import api.main.models.User;
import api.main.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    UserRepository userRepository;
    AuthService(UserRepository _userRepository) {
        userRepository = _userRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }
}
