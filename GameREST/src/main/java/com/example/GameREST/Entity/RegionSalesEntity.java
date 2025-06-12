package com.example.GameREST.Entity;


import com.example.GameREST.DTO.RegionSalesId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "region_sales", schema = "video_games")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegionSalesEntity {

    @EmbeddedId
    private RegionSalesId id;

    @MapsId("regionId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id",referencedColumnName = "id")
    private RegionEntity region;

    @MapsId("gamePlatformId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_platform_id", referencedColumnName = "id")
    private GamePlatformEntity gamePlatform;

    @Column(name = "num_sales")
    private int numSales;
}


