package api.main.dtos.auth;

public record AuthResponseDto(String token, UserDto user) {
}