package com.example.Greenswamp.Repository;


import com.example.Greenswamp.Entity.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction,Long> {

    @Query("SELECT count(i.id) FROM Interaction i WHERE  i.interactionType = :nameInteraction")
    public Long CountInteractionWithName(@Param("nameInteraction") Interaction.InteractionType name);

    @Query("SELECT count(i.id) FROM Interaction i WHERE  i.interactionType = :nameInteraction AND i.post.id = :postId")
    public Long CountInteractionWithNameAndPost(@Param("nameInteraction") Interaction.InteractionType name, @Param("postId") Long postId);



}
