package com.example.Greenswamp.Services;


import com.example.Greenswamp.Entity.Interaction;
import com.example.Greenswamp.Repository.InteractionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InteractionServiceImp implements InteractionService{

    @Autowired
    private InteractionRepository interactionRepository;


    @Override
    public Long CountInteractionWithNameAndPost(Interaction.InteractionType name, Long postId) {
        return interactionRepository.CountInteractionWithNameAndPost(name,postId);
    }

    @Override
    public Long CountInteractionWithName(Interaction.InteractionType name) {
        return interactionRepository.CountInteractionWithName(name);
    }

}
