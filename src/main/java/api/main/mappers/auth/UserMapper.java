package api.main.mappers.auth;

import api.main.dtos.auth.RegisterRequestDto;
import api.main.dtos.auth.UserDto;
import api.main.models.User;

public class UserMapper {
    public User toUser(RegisterRequestDto requestDto) {
        return new User(requestDto.name(), requestDto.email(), requestDto.password());
    }
    
    public UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }
}
