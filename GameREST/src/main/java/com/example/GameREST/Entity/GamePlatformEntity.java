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
    @JoinColumn(name = "game_publisher_id",referencedColumnName = "id")
    private GamePublisherEntity gamePublisher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "platform_id")
    private PlatformEntity platform;

    @OneToMany(mappedBy = "gamePlatform",cascade = CascadeType.REMOVE)
    private List<RegionSalesEntity> regionSalesEntities;

    @Column(name = "release_year")
    private  int releaseYear;
}
