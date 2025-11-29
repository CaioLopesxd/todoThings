package api.main.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDto(@NotBlank(message = "Preencha o nome.") String name,
                                 @Email(message = "Preencha um email valido.") String email,
                                 @NotBlank(message = "Preencha a Senha") String password) {
}
