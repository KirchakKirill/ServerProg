package com.example.GameREST.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для создания связи [Игра - Платформа]")
public class RequestGamePlatformDTO {

    @Schema(description = "Название игры (если такая игра уже записана, то попытка переписать её жанр вызовет ошибку)",
            example = "The Witcher 4 ",
            minLength = 3,
            maxLength = 150)
    @NotBlank
    @Size(min = 3, max = 150, message = "[Некорректная длина] (мин = 3, макс = 150)")
    private String gameName;


    @Schema(description = "Название издателя",
            example = "NoNamePublisher",
            minLength = 3,
            maxLength = 50)
    @NotBlank
    @Size(min = 3, max = 50, message = "В списке нет таких издателей [Некорректная длина]")
    private String publisherName;


    @Schema(description = "Дата релиза",
            example = "2025",
            minLength = 4,
            minimum = "1958",
            maximum = "2025")
    @NotNull
    @Min(value = 1958,message = "Первая игра вышла в 1958)")
    @Max(value = 2025,message = "Игры,которые выйдут в будущем не рассматриваются")
    private Integer releaseYear;


    @Schema(description = "Название платформы",
            example = "PS4",
            minLength = 2,
            maxLength = 4
            )
    @NotBlank
    @Size(min = 2, max = 4, message = "Некорректная длина")
    private String platformName;


    @Schema(description = "Название жанра",
            example = "Action/RPG"
    )
    @NotNull
    private String genreName;


}
