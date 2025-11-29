package api.main.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(@Email(message = "Email deve estar em um formato valido.") String email,
                              @NotBlank(message = "Preencha a senha.") String password) {
}