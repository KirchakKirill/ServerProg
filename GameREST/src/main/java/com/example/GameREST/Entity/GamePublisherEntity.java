package com.example.GameREST.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "game_publisher", schema = "video_games")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GamePublisherEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id",
                foreignKey = @ForeignKey(
                        name = "fk_gpu_gam",
                        foreignKeyDefinition = "FOREIGN KEY (game_id) REFERENCES video_games.game(id)" +
                                "ON DELETE CASCADE"
                ))
    private GameEntity game;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id",
                foreignKey = @ForeignKey(
                        name = "fk_gpu_pub",
                        foreignKeyDefinition = "FOREIGN KEY (publisher_id) REFERENCES video_games.publisher(id)" +
                                "ON DELETE CASCADE"
                ))
    private PublisherEntity publisher;
}
