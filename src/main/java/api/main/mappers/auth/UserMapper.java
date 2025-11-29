package api.main.mappers.auth;

import api.main.dtos.auth.ContactDto;
import api.main.dtos.auth.RegisterRequestDto;
import api.main.dtos.auth.UserDto;
import api.main.models.User;

import java.util.List;

public class UserMapper {
    public User toUser(RegisterRequestDto requestDto) {
        return new User(requestDto.name(), requestDto.email(), requestDto.password());
    }
    
    public UserDto toUserDto(User user) {
        List<ContactDto> contactDtos = user.getContacts()
                .stream()
                .map(c -> new ContactDto(c.getName(), c.getEmail()))
                .toList();
        return new UserDto(user.getId(), user.getName(), user.getEmail(),contactDtos);
    }
}
