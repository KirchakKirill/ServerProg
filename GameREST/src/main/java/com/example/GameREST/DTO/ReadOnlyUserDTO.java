package com.example.GameREST.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для получения информации о пользователях и их правах (для администрации)")
public class ReadOnlyUserDTO {


    @Schema(description = "идентификатор пользователя")
    private Long id;
    @Schema(description = "имя пользователя")
    private String username;
    @Schema(description = "роль пользователя")
    private String authority;

}
