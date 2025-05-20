package com.example.Greenswamp.Repository;


import com.example.Greenswamp.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long>
{
    @Query(value = "select * from posts",nativeQuery = true)
    List<Post> GetPostsOrderByCreatedAt();

    @Query(value = "select * from posts where user_id = :userId",nativeQuery = true)
    List<Post> findAllPostsByUser(@Param("userId") Long userId);

    @Query(value = "select * from posts where post_id = :postId",nativeQuery = true)
    Optional<Post> findPostById(@Param("postId") Long postId);


}
