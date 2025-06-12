package com.example.GameREST.DTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestGameDTO {

    @NotBlank
    @Size(min = 3)
    private String gameName;

    @NotBlank
    @Size(min = 3)
    private String genreName;
}
