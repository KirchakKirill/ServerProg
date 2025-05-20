package com.example.Greenswamp.Repository;


import com.example.Greenswamp.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
    @Query("select u from User u")
    public List<User> getAllUser();

    @Query("select u from User u where u.id = :userId")
    public Optional<User> findUserById(@Param("userId") Long userId);
}
