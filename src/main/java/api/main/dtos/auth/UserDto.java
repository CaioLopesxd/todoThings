package api.main.dtos.auth;

import java.util.List;

public record UserDto(String name, String email, List<ContactDto> contacts) {
}
