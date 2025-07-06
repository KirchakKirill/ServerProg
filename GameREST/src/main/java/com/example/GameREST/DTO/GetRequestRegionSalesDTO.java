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
@Schema(description = "DTO для поиска информации о продажах в регионе по идентификатору")
public class GetRequestRegionSalesDTO {

    @NotNull
    @Positive(message = "Идентификатор должен быть положителен")
    @Schema(description = "id региона", example = "1")
    private Long regionId;

    @NotNull
    @Positive(message = "Идентификатор должен быть положителен")
    @Schema(description = "id связи Игра-Платформа",example = "50")
    private Long gamePlatformId;

}
