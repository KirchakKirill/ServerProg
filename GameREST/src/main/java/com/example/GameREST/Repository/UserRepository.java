package com.example.GameREST.Repository;

import com.example.GameREST.Entity.UserEntity;
import liquibase.license.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.config.annotation.web.PortMapperDsl;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    @Query("select u from UserEntity u where u.username = :username")
    Optional<UserEntity> findByUserName(@Param("username") String username);


    @Query("select count(u) > 0 from UserEntity u where u.username = :username")
    boolean existsByUserName(@Param("username") String username);
}
