package com.example.GameREST.Repository;

import com.example.GameREST.Entity.UserAuthority;
import com.example.GameREST.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    @Query("select u from UserEntity u where u.username = :username")
    Optional<UserEntity> findByUserName(@Param("username") String username);

    @Query("select count(u) > 0 from UserEntity u where u.username = :username")
    boolean existsByUserName(@Param("username") String username);

    @Query(value = "SELECT u FROM UserEntity u JOIN FETCH u.authorities WHERE u.id = :id")
    Optional<UserEntity> findByIdWithAuthorities(@Param("id") Long id);

    @Query("select a from UserAuthority a where a.authority= :authority")
    Optional<UserAuthority> findAuthorityByName(@Param("authority") String authority);


    @Query("select u from UserEntity u join fetch u.authorities where u.username = :username")
    Optional<UserEntity> findByUserNameWithAuthorities(@Param("username") String username);
}
