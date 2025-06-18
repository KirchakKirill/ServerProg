package com.example.GameREST.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_user_authority",schema = "video_games")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "c_authority",nullable = false)
    private String authority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user",nullable = false)
    private UserEntity user;

    public UserAuthority(String authority, UserEntity user)
    {
        this.authority = authority;
        this.user = user;
    }

}