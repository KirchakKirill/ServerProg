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
@Schema(description = "DTO для отображения информации о регионе")
public class RegionDTO
{
    @Schema(description = "идентификатор региона",example = "3")
    private Long id;
    @Schema(description = "Japan")
    private String regionName;
}
