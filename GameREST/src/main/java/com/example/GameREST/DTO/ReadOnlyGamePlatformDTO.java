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
@Schema(description = "DTO для отображения информации об Игре-Платформе")
public class ReadOnlyGamePlatformDTO {

    @Schema(description = "идентификатор", example= "1")
    private Long id;
    @Schema(description = "Название игры", example = "Panzer Tactics")
    private String gameName;
    @Schema(description = "Название издателя", example = "10TACLE Studios")
    private String publisherName;
    @Schema(description = "Дата релиза", example = "2007")
    private Integer releaseYear;
    @Schema(description = "Название платформа", example = "DS")
    private String platformName;
    @Schema(description = "Название жанра", example = "Strategy")
    private String genreName;
}
