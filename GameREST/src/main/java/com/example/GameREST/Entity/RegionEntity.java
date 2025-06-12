package com.example.GameREST.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "region", schema = "video_games")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name = "region_name",nullable = false,unique = true)
    private String regionName;

    @OneToMany(mappedBy = "region",cascade = CascadeType.PERSIST)
    private List<RegionSalesEntity> regionSalesEntities;


}
