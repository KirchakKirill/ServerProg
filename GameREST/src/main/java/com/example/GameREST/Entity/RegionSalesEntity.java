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
    @JoinColumn(name = "region_id",
                foreignKey = @ForeignKey(
                        name = "fk_rs_reg",
                        foreignKeyDefinition = "FOREIGN KEY (region_id) REFERENCES" +
                                "video_games.region(id) ON DELETE CASCADE"
                ))
    private RegionEntity region;

    @MapsId("gamePlatformId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_platform_id",
                foreignKey = @ForeignKey(
                        name = "fk_rs_gp",
                        foreignKeyDefinition = "FOREIGN KEY (game_platform_id) REFERENCES " +
                                "video_games.game_platform(id) ON DELETE CASCADE"
                ))
    private GamePlatformEntity gamePlatform;

    @Column(name = "num_sales")
    private float numSales;
}


