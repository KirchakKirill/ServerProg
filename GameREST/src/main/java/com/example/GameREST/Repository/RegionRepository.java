package com.example.GameREST.Repository;

import com.example.GameREST.Entity.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<RegionEntity,Long> {
    @Query("select r from RegionEntity r where r.regionName = :regionName")
    Optional<RegionEntity> findByRegionName(@Param("regionName") String regionName);

    @Query("SELECT COUNT(rs) FROM RegionSalesEntity rs WHERE rs.region = :region")
    Integer existsRegionByRegionSales(@Param("region") RegionEntity region);
}
