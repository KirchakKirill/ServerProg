package com.example.Greenswamp.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(name = "content", nullable = false)
    private String contentE;

    @Column(name = "post_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostType postType;

    @Column(name = "media_url")
    private String mediaUrl;

    @Column(name = "media_type")
    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    @Column(name = "alt_text")
    private String altText;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_post_id")
    private Post parentPost;

    @OneToMany(mappedBy = "parentPost")
    private List<Post> replies = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Interaction> interactions = new ArrayList<>();

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL)
    private Event event;

    public Post() {
    }

    public Post(Long id, User user, String contentE, PostType postType, String mediaUrl,
                MediaType mediaType, String altText, String thumbnailUrl,
                LocalDateTime createdAt, Post parentPost) {
        this.id = id;
        this.user = user;
        this.contentE = contentE;
        this.postType = postType;
        this.mediaUrl = mediaUrl;
        this.mediaType = mediaType;
        this.altText = altText;
        this.thumbnailUrl = thumbnailUrl;
        this.createdAt = createdAt;
        this.parentPost = parentPost;
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

    public String getContentE() {
        return contentE;
    }

    public void setContentE(String contentE) {
        this.contentE = contentE;
    }

    public PostType getPostType() {
        return postType;
    }

    public void setPostType(PostType postType) {
        this.postType = postType;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Post getParentPost() {
        return parentPost;
    }

    public void setParentPost(Post parentPost) {
        this.parentPost = parentPost;
    }

    public List<Post> getReplies() {
        return replies;
    }

    public void setReplies(List<Post> replies) {
        this.replies = replies;
    }

    public List<Interaction> getInteractions() {
        return interactions;
    }

    public void setInteractions(List<Interaction> interactions) {
        this.interactions = interactions;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    // Enum определения
    public enum PostType {
        text, image, video
    }

    public enum MediaType {
        image, video
    }

    // Метод валидации
    @AssertTrue
    private boolean isMediaValid() {
        return (postType == PostType.text && mediaUrl == null) ||
                (postType != PostType.text && mediaUrl != null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        // Сравниваем основные поля, которые должны быть одинаковыми для одинаковых постов
        if (!Objects.equals(id, post.id)) return false;
        if (!Objects.equals(contentE, post.contentE)) return false;
        if (postType != post.postType) return false;
        if (!Objects.equals(createdAt, post.createdAt)) return false;

        // Для пользователя сравниваем только ID, чтобы избежать рекурсии
        if (user != null && post.user != null && !Objects.equals(user.getId(), post.user.getId())) {
            return false;
        }

        // Для родительского поста сравниваем только ID
        if (parentPost != null && post.parentPost != null && !Objects.equals(parentPost.getId(), post.parentPost.getId())) {
            return false;
        } else if ((parentPost == null) != (post.parentPost == null)) {
            return false;
        }

        // Сравниваем медиа-поля только для медиа-постов
        if (postType != PostType.text) {
            if (!Objects.equals(mediaUrl, post.mediaUrl)) return false;
            if (mediaType != post.mediaType) return false;
            if (!Objects.equals(altText, post.altText)) return false;
            if (!Objects.equals(thumbnailUrl, post.thumbnailUrl)) return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, contentE, postType, createdAt);

        // Включаем ID пользователя и родительского поста в хеш-код
        result = 31 * result + (user != null ? Objects.hash(user.getId()) : 0);
        result = 31 * result + (parentPost != null ? Objects.hash(parentPost.getId()) : 0);

        // Для медиа-постов включаем медиа-поля в хеш-код
        if (postType != PostType.text) {
            result = 31 * result + Objects.hash(mediaUrl, mediaType, altText, thumbnailUrl);
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Post{");
        sb.append("id=").append(id);
        sb.append(", user=").append(user != null ? user.getId() : "null");
        sb.append(", content='").append(contentE).append('\'');
        sb.append(", postType=").append(postType);

        if (postType != PostType.text) {
            sb.append(", mediaUrl='").append(mediaUrl).append('\'');
            sb.append(", mediaType=").append(mediaType);
            if (altText != null) sb.append(", altText='").append(altText).append('\'');
            if (thumbnailUrl != null) sb.append(", thumbnailUrl='").append(thumbnailUrl).append('\'');
        }

        sb.append(", createdAt=").append(createdAt);
        sb.append(", parentPost=").append(parentPost != null ? parentPost.getId() : "null");
        sb.append(", repliesCount=").append(replies != null ? replies.size() : 0);
        sb.append(", interactionsCount=").append(interactions != null ? interactions.size() : 0);
        sb.append(", event=").append(event != null ? "exists" : "null");
        sb.append('}');

        return sb.toString();
    }
}