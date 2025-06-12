package com.example.GameREST.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestGamePlatformDTO {

    @NotBlank
    @Size(min = 3, max = 150, message = "[Некорректная длина] (мин = 3, макс = 150)")
    private String gameName;


    @NotBlank
    @Size(min = 3, max = 50, message = "В списке нет таких издателей [Некорректная длина]")
    private String publisherName;

    @NotNull
    @Min(value = 1958,message = "Первая игра вышла в 1958)")
    @Max(value = 2025,message = "Игры,которые выйдут в будущем не рассматриваются")
    private Integer releaseYear;

    @NotBlank
    @Size(min = 2, max = 4, message = "Некорректная длина")
    private String platformName;

    @NotBlank
    private String genreName;
}
