package com.example.GameREST.Repository;

import com.example.GameREST.Entity.UserAuthority;
import com.example.GameREST.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAuthorityRepository extends JpaRepository<UserAuthority,Long> {


    @Query(value = "SELECT a FROM t_user_authority a WHERE a.id_user = :id",nativeQuery = true)
    List<UserAuthority> findAuthoritiesByUser(@Param("id") Long id);
}
