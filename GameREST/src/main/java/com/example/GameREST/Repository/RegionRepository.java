package com.example.GameREST.Repository;

import com.example.GameREST.Entity.RegionEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Qualifier("regionRepository")
public interface RegionRepository extends GeneralRepository<RegionEntity,Long> {
    @Override
    @Query("select r from RegionEntity r where r.regionName = :regionName")
    Optional<RegionEntity> findEntityByName(@Param("regionName") String regionName);

    @Override
    @Query("select r.id from RegionEntity r where r.regionName = :regionName")
    Optional<Long> findIdByName(@Param("regionName") String regionName);

    @Override
    @Query("SELECT COUNT(rs) FROM RegionSalesEntity rs WHERE rs.region = :region")
    Integer countLinksForEntity(@Param("region") RegionEntity region);
}
