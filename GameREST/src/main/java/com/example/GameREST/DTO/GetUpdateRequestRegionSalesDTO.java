package com.example.GameREST.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
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
@Schema(description = "DTO для изменения записи о продажах в регионе")
public class GetUpdateRequestRegionSalesDTO
{
    @NotNull
    @Positive(message = "Идентификатор должен быть положителен")
    @Schema(description = "id региона", example = "1")
    private Long oldRegionId;

    @NotNull
    @Positive(message = "Идентификатор должен быть положителен")
    @Schema(description = "id связи Игра-Платформа",example = "50")
    private Long oldGamePlatformId;


    @NotNull
    @Positive(message = "Идентификатор должен быть положителен")
    @Schema(description = "id региона", example = "1")
    private Long newRegionId;

    @NotNull
    @Positive(message = "Идентификатор должен быть положителен")
    @Schema(description = "id связи Игра-Платформа",example = "50")
    private Long newGamePlatformId;

    @NotNull
    @Min(0)
    @Schema(description = "Продажи", example = "3.50")
    private float numSales;

}
