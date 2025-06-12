package com.example.GameREST.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "platform", schema = "video_games")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlatformEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name = "platform_name",nullable = false,unique = true)
    private String platformName;

    @OneToMany(mappedBy = "platform")
    private List<GamePlatformEntity> gamePlatformEntities;
}
