package com.example.GameREST.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "t_deactivated_token",schema = "video_games")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeactivatedToken {
    @Id
    private UUID id;

    @Column(name = "c_keep_until")
    private Instant keepUntil;


}