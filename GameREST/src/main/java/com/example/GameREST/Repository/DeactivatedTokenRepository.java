package com.example.GameREST.Repository;

import com.example.GameREST.Entity.DeactivatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeactivatedTokenRepository extends JpaRepository<DeactivatedToken, UUID> {

}
