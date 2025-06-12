package com.example.GameREST.Repository;

import com.example.GameREST.Entity.PublisherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublisherRepository extends JpaRepository<PublisherEntity,Long> {

    @Query("select p from PublisherEntity p where p.id = :id")
    Optional<PublisherEntity> findPublisherById(@Param("id") Long id);

    @Query("select p from PublisherEntity p where p.publisherName = :publisherName")
    Optional<PublisherEntity> findPublisherByName(@Param("publisherName") String publisherName);

    @Modifying
    @Query("Update PublisherEntity p set p.publisherName = :publisherName where p.id = :id")
    void update(@Param("id") Long id, @Param("publisherName") String publisherName);

}
