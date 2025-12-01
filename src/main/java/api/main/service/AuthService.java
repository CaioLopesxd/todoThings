package api.main.service;

import api.main.dtos.auth.NewContactDto;
import api.main.dtos.auth.UpdateUserDto;
import api.main.dtos.auth.UserDto;
import api.main.exceptions.auth.ContactAlreadyExists;
import api.main.exceptions.auth.EmailOrPasswordError;
import api.main.exceptions.auth.UserNotAuthenticated;
import api.main.exceptions.auth.UserNotFound;
import api.main.mappers.auth.UserMapper;
import api.main.models.User;
import api.main.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    public AuthService(UserRepository _userRepository, PasswordEncoder _passwordEncoder, JwtService _jwtService, UserMapper _userMapper) {
        this.userRepository = _userRepository;
        this.passwordEncoder = _passwordEncoder;
        this.jwtService = _jwtService;
        this.userMapper = _userMapper;
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getAuthenticatedUser(UUID id) {
        return userRepository.findById(id).orElseThrow();
    }


    public User authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFound::new);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new EmailOrPasswordError();
        }

        return user;
    }

    public String generateTokenForUser(User user) {
        return jwtService.generateToken(user.getId());
    }

    public User addContact(User _owner, NewContactDto newContactDto){
        User owner = userRepository.findById(_owner.getId()).orElseThrow(UserNotAuthenticated::new);
        User contact = userRepository.findByEmail(newContactDto.email()).orElseThrow(UserNotFound::new);

        if(userRepository.existsByIdAndContacts_Id(owner.getId(),contact.getId()))
            throw new ContactAlreadyExists();

        owner.getContacts().add(contact);
        return userRepository.save(owner);
    }

    public UserDto updateUser(UpdateUserDto updateUserDto, User user){
        User updatedUser = userRepository.findById(user.getId()).orElseThrow(UserNotFound::new);

        if(updateUserDto.name() != null) updatedUser.setName(updateUserDto.name());

        userRepository.save(updatedUser);

        return userMapper.toUserDto(updatedUser);
    }
}
