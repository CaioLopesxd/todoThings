package api.main.dtos.auth;

import java.util.UUID;

public record UserDto(UUID id, String name, String email) {
}
