package com.example.GameREST.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadOnlyGamePlatformDTO {

    private Long id;
    private String gameName;
    private String publisherName;
    private Integer releaseYear;
    private String platformName;
    private String genreName;
}
