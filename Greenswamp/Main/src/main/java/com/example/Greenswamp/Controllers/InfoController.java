package com.example.Greenswamp.Controllers;

import com.example.Greenswamp.Entity.Event;
import com.example.Greenswamp.Entity.Interaction;
import com.example.Greenswamp.Entity.Post;
import com.example.Greenswamp.Entity.User;
import com.example.Greenswamp.Services.EventService;
import com.example.Greenswamp.Services.InteractionService;
import com.example.Greenswamp.Services.PostService;
import com.example.Greenswamp.Services.UserService;
import jakarta.persistence.Access;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Role;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.beans.BeanDescriptor;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class InfoController {

    @Autowired
    private PostService postService;

    @Autowired
    private EventService eventService;

    @Autowired
    private InteractionService interactionService;

    @Autowired
    private UserService userService;




    private Map<Post, List<Pair<Interaction.InteractionType, Long>>> createInterMap(List<Post> posts) {
        Map<Post, List<Pair<Interaction.InteractionType, Long>>> interMap = new LinkedHashMap<>();
        for (Post post : posts) {
            List<Pair<Interaction.InteractionType, Long>> interactions = new ArrayList<>();
            for (Interaction.InteractionType type : Interaction.InteractionType.values()) {
                Long count = interactionService.CountInteractionWithNameAndPost(type, post.getId());
                interactions.add(new Pair<>(type, count));
            }
            interMap.put(post, interactions);
        }
        return interMap;
    }

    private Map<Event, Long> createEventMap(List<Event> events) {
        Map<Event, Long> eventMap = new LinkedHashMap<>();
        for (Event event : events) {
            Long count = interactionService.CountInteractionWithNameAndPost(
                    Interaction.InteractionType.rsvp,
                    event.getPost().getId()
            );
            eventMap.put(event, count);
        }
        return eventMap;
    }



    @GetMapping("/feed")
    public String posts(Model model) {

        List<Post> posts = postService.getAllPosts();
        List<Event> events = eventService.getAllEventsWithContent();

        posts.removeIf(p -> events.stream()
                .map(e -> e.getPost().getId())
                .toList()
                .contains(p.getId()));


        List<Object> allElements = new ArrayList<>();
        allElements.addAll(posts);
        allElements.addAll(events);


        allElements.sort((o1, o2) -> {
            LocalDateTime date1 = (o1 instanceof Post) ? ((Post) o1).getCreatedAt()
                    : ((Event) o1).getPost().getCreatedAt();
            LocalDateTime date2 = (o2 instanceof Post) ? ((Post) o2).getCreatedAt()
                    : ((Event) o2).getPost().getCreatedAt();
            return date2.compareTo(date1); // Сортировка по убыванию (новые сначала)
        });



        Map<Post, List<Pair<Interaction.InteractionType, Long>>> interMap = createInterMap(posts);
        Map<Event, Long> eventMap = createEventMap(events);



        model.addAttribute("elements", allElements);
        model.addAttribute("interMap", interMap);
        model.addAttribute("eventMap", eventMap);
        return "feed";
    }

    @GetMapping("/profile/{userId}")
    public String getProfile(@PathVariable Long userId, Model model){

        List<Post> posts = postService.getAllPosts();
        List<Event> events = eventService.getAllEventsWithContent();

        Optional<User> u = userService.findUserById(userId);
        User userCurrent;
        if (u.isPresent()){
            userCurrent = u.get();
        }
        else {
            throw new NullPointerException("User is NULL");
        }

        List<Post> rabbits = postService.findAllPostsByUser(userCurrent.getId());
        List<Post> media = rabbits.stream().filter(p-> p.getPostType().equals(Post.PostType.image)
                ||
                p.getPostType().equals(Post.PostType.video)).toList();
        List<Post> repliesAll = new ArrayList<>();
        rabbits.forEach(p->repliesAll.addAll(p.getReplies()));
        repliesAll.forEach(e->System.out.println(e.getId()));


        Map<Post, List<Pair<Interaction.InteractionType, Long>>> interMap = createInterMap(posts);
        Map<Event, Long> eventMap = createEventMap(events);

        model.addAttribute("user",userCurrent);
        model.addAttribute("rabbits",rabbits);
        model.addAttribute("media",media);
        model.addAttribute("repliesAll",repliesAll);
        model.addAttribute("interMap", interMap);
        model.addAttribute("eventMap", eventMap);
        return "profile";
    }

    @PostMapping("/profile") // сделать валидацию
    public String setProfile(@RequestParam("postId") String postId){
        Long id  = Long.parseLong(postId);
       var post =  postService.findPostById(id);
       Post postCurrent;
       if (post.isPresent())
       {
           postCurrent = post.get();
       }
       else{
           throw new NullPointerException("post is NULL");
       }

        var userId =  postCurrent.getUser().getId();
        return "redirect:/profile/" + userId;
    }

    @GetMapping("/feed/post/{postId}")
    public String getPostById(@PathVariable Long postId,Model model){

        var post =  postService.findPostById(postId);
        Post postCurrent;
        if (post.isPresent())
        {
            postCurrent = post.get();
        }
        else{
            throw new NullPointerException("There is no post with this ID");
        }
        Long commentCount = postCurrent.getInteractions().stream().filter(i->i.getInteractionType() == Interaction.InteractionType.comment).count();
        Long reribbCount = postCurrent.getInteractions().stream().filter(i->i.getInteractionType() == Interaction.InteractionType.reribb).count();
        model.addAttribute("post",postCurrent);
        model.addAttribute("commentCount",commentCount);
        model.addAttribute("reribbCount",reribbCount);
        return "post";
    }

    @PostMapping("/feed/post")
    public String feedPost(@RequestParam("PostIdentification") String postId ){
        Long id  = Long.parseLong(postId);
        return "redirect:/feed/post/" + id;
    }


}
