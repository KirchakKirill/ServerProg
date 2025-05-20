package com.example.Greenswamp.Services;


import com.example.Greenswamp.Entity.Interaction;
import org.springframework.stereotype.Service;

@Service
public interface InteractionService {

    public Long CountInteractionWithNameAndPost(Interaction.InteractionType name, Long postId);

    public Long CountInteractionWithName(Interaction.InteractionType name);
}
