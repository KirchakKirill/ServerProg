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
@Schema(description = "DTO для отображения информации о платформах")
public class PlatformDTO {

    @Schema(description = "идентификатор платформы",example = "15")
    private Long id;
    @Schema(description = "Название платформы",example = "PC")
    private String platformName;
}
