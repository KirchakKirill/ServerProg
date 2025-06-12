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
    @JoinColumn(name = "game_id",referencedColumnName = "id")
    private GameEntity game;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id",referencedColumnName = "id")
    private PublisherEntity publisher;

    @OneToMany(mappedBy = "gamePublisher",cascade = CascadeType.REMOVE)
    private  List<GamePlatformEntity> gamePlatformEntities;
}
