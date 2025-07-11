package com.example.GameREST.Repository;

import com.example.GameREST.Entity.PlatformEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Qualifier("platformRepository")
public interface PlatformRepository extends GeneralRepository<PlatformEntity,Long> {

    @Override
    @Query("select p from PlatformEntity p where p.platformName = :name")
    Optional<PlatformEntity> findEntityByName(@Param("name") String name);

    @Modifying
    @Query("Update PlatformEntity p set p.platformName = :platformName where p.id = :id")
    void update(@Param("id") Long id, @Param("platformName") String platformName);

    @Override
    @Query("SELECT COUNT(gp) FROM GamePlatformEntity gp WHERE gp.platform = :platform")
    Integer countLinksForEntity(@Param("platform") PlatformEntity platform);

    @Override
    @Query("select p.id from PlatformEntity p where p.platformName = :name")
    Optional<Long> findIdByName(@Param("name") String name);

}
