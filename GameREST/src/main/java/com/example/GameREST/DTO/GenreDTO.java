package com.example.GameREST.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для отображения информации о жанрах")
public class GenreDTO {

    @Schema(description = "идентификатор жанра",example = "1")
    private Long id;
    @Schema(description = "Название жанра",example = "Action")
    private  String genreName;
}
