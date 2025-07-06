package com.example.GameREST.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для регистрации/логина")
public class UserDTO {

    @Schema(description = "имя пользователя")
    @NotBlank
    @Size(min=3,max = 50)
    private String username;

    @Schema(description = "пароль")
    @NotBlank
    @Size(min=8,max = 50)
    private String password;

}
