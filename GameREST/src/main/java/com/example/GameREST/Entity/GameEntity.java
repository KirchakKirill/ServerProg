package com.example.GameREST.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "game", schema = "video_games")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"genre", "gamePublisherEntities"})
public class GameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name = "game_name")
    private String gameName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private GenreEntity genre;

    @OneToMany(mappedBy = "game",cascade = CascadeType.REMOVE)
    private List<GamePublisherEntity> gamePublisherEntities;


}
