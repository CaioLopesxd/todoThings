package api.main.dtos.auth;

import api.main.models.User;

import java.util.List;
import java.util.UUID;

public record UserDto(UUID id, String name, String email, List<ContactDto> contacts) {
}
