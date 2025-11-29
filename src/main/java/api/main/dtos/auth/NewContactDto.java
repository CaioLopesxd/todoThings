package api.main.dtos.auth;

import jakarta.validation.constraints.NotBlank;

public record NewContactDto (@NotBlank(message = "Email não pode ser vazio.") String email){
}