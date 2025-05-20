package com.example.Greenswamp.Services;

import com.example.Greenswamp.Entity.Post;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface PostService {

    public List<Post> getAllPosts();

    List<Post> findAllPostsByUser( Long userId);

    Optional<Post> findPostById(Long postId);

}
