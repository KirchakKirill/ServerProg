package com.example.GameREST.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.Type;

import java.util.List;

@Entity
@Table(name = "publisher", schema = "video_games")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublisherEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name = "publisher_name",nullable = false,unique = true)
    private String publisherName;

    @OneToMany(mappedBy = "publisher",cascade = CascadeType.REMOVE)
    private List<GamePublisherEntity> publisherEntities;
}
