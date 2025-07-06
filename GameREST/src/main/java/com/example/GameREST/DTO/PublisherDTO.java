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
@Schema(description = "DTO для отображения информации об издателях")
public class PublisherDTO
{
    @Schema(description = "идентификатор издателя",example = "1")
    private Long id;
    @Schema(description = "Название издателя", example = "10TACLE Studios")
    private String publisherName;
}
