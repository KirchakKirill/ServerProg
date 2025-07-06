package com.example.GameREST.Repository;

import com.example.GameREST.Entity.PlatformEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlatformRepository extends JpaRepository<PlatformEntity,Long> {

    @Query("select p from PlatformEntity p where p.platformName = :name")
    Optional<PlatformEntity> findPlatformByName(@Param("name") String name);

    @Modifying
    @Query("Update PlatformEntity p set p.platformName = :platformName where p.id = :id")
    void update(@Param("id") Long id, @Param("platformName") String platformName);

    @Query("SELECT COUNT(gp) FROM GamePlatformEntity gp WHERE gp.platform = :platform")
    Integer existsGamePlatformByPlatform(@Param("platform") PlatformEntity platform);

}
