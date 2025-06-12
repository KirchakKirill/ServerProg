package com.example.Greenswamp.Repository;

import com.example.Greenswamp.Entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority,Long> {

    @Query("select a from Authority a where a.authorityType = :type")
    public Authority findByAuthorityType(@Param("type")Authority.AuthorityType type);
}
