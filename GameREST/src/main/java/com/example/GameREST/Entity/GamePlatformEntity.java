package com.example.GameREST.Entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Field;
import java.util.List;

@Entity
@Table(name = "game_platform", schema = "video_games")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GamePlatformEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_publisher_id",
                foreignKey = @ForeignKey(
                        name = "fk_gpl_gp",
                        foreignKeyDefinition = "FOREIGN KEY (game_publisher_id) REFERENCES " +
                                "video_games.game_publisher(id) ON DELETE CASCADE"
                ))
    private GamePublisherEntity gamePublisher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "platform_id",
                    foreignKey = @ForeignKey(
                            name = "fk_gpl_pla",
                            foreignKeyDefinition = "FOREIGN KEY (platform_id) REFERENCES video_games.platform(id)" +
                                    "ON DELETE CASCADE"
                    ))
    private PlatformEntity platform;

    @Column(name = "release_year")
    private  int releaseYear;
}
