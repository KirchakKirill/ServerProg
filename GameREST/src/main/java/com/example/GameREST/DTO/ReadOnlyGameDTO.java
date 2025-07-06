package com.example.GameREST.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для отображения информации об игре")
public class ReadOnlyGameDTO {
    @Schema(description = "идентификатор игры", example = "17")
    private Long id;
    @Schema(description = "Название игры", example = "1/2 Summer +")
    private String gameName;
    @Schema(description = "Жанр игры", example = "Adventure")
    private String genreName;

}
