package com.example.GameREST.DTO;


import com.example.GameREST.Entity.GamePlatformEntity;
import com.example.GameREST.Entity.RegionEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для отображения информации о продажах в регионе")
public class RegionSalesDTO {

    @NotNull
    @Positive
    @Schema(description = "идентификатор региона", example = "1")
    private Long regionId;

    @NotNull
    @Positive
    @Schema(description = "идентификатор связи Игра-Платформа",example = "50")
    private Long gamePlatformId;

    @NotNull
    @Positive(message = "Количество продаж должно быть положительным числом")
    @Schema(description = "Продажи", example = "3.50")
    private float numSales;
}
