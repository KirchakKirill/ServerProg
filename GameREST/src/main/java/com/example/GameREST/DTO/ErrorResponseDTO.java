package com.example.GameREST.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель ошибки API")
public class ErrorResponseDTO
{
    @Schema(description = "HTTP статус код", example = "404")
    private int status;

    @Schema(description = "Тип ошибки", example = "Not Found")
    private String error;

    @Schema(description = "Детали ошибки (может быть строкой или объектом)",example = "Ресурс не найден")
    private Object message;

    @Schema(description = "Время возникновения ошибки", example = "2025-07-05T12:00:00Z")
    private Date timestamp;
}
