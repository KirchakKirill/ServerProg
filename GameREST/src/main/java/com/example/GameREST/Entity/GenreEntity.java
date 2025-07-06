package com.example.GameREST.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "genre", schema = "video_games")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenreEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name = "genre_name",nullable = false,unique = true)
    private String genreName;

}
