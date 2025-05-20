package com.example.Greenswamp.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "interactions", uniqueConstraints =
@UniqueConstraint(columnNames = {"user_id", "post_id", "interaction_type"}))
public class Interaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interaction_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    @Column(name = "interaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private InteractionType interactionType;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public enum InteractionType {
        comment, reribb, like, rsvp
    }

    // Конструкторы
    public Interaction() {
    }

    public Interaction(Long id, User user, Post post, InteractionType interactionType, String content, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.post = post;
        this.interactionType = interactionType;
        this.content = content;
        this.createdAt = createdAt;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public InteractionType getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(InteractionType interactionType) {
        this.interactionType = interactionType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // equals и hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interaction that = (Interaction) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(user, that.user) &&
                Objects.equals(post, that.post) &&
                interactionType == that.interactionType &&
                Objects.equals(content, that.content) &&
                Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, post, interactionType, content, createdAt);
    }

    // toString
    @Override
    public String toString() {
        return "Interaction{" +
                "id=" + id +
                ", user=" + user +
                ", post=" + post +
                ", interactionType=" + interactionType +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}