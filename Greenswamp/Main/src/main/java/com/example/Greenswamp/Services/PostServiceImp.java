package com.example.Greenswamp.Services;

import com.example.Greenswamp.Entity.Post;
import com.example.Greenswamp.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImp implements PostService{

    @Autowired
    private PostRepository postRepository;
    @Override
    public List<Post> getAllPosts() {
        return postRepository.GetPostsOrderByCreatedAt();
    }

    public List<Post> findAllPostsByUser( Long userId){
        return postRepository.findAllPostsByUser(userId);
    }

    @Override
    public Optional<Post> findPostById(Long postId) {
        return postRepository.findPostById(postId);
    }
}
