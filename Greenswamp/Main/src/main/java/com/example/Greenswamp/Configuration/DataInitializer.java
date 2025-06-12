package com.example.Greenswamp.Configuration;

import com.example.Greenswamp.Entity.Authority;
import com.example.Greenswamp.Entity.User;
import com.example.Greenswamp.Repository.AuthorityRepository;
import com.example.Greenswamp.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;

    public DataInitializer(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Authority userAuthority = authorityRepository.findByAuthorityType(Authority.AuthorityType.USER);
        Authority adminAuthority = authorityRepository.findByAuthorityType(Authority.AuthorityType.ADMIN);
        Authority moderatorAuthority = authorityRepository.findByAuthorityType(Authority.AuthorityType.MODERATOR);

        // Создание пользователя 1
        User user1 = new User();
        user1.setUsername("Kiri11");
        user1.setEmail("kirya.kirchak@mail.ru");
        user1.setDisplayName("@kir_cha");
        user1.setAvatarUrl("https://i.pravatar.cc/100?u=dorm29_frogs@pravatar.com");
        user1.setPassword(passwordEncoder.encode("Nokia111!"));
        user1.setBio("my biography ...");
        user1.setAuthority(List.of(adminAuthority));

        // Создание пользователя 2
        User user2 = new User();
        user2.setUsername("FroggyFriends");
        user2.setEmail("Froggy@gmail.com");
        user2.setDisplayName("@dorm23_frogs");
        user2.setAvatarUrl("https://i.pravatar.cc/100?u=dorm23_frogs@pravatar.com");
        user2.setPassword(passwordEncoder.encode("Frogsss123@"));
        user2.setBio("my biography1 ...");
        user2.setAuthority(List.of(moderatorAuthority));

        // Создание пользователя 3
        User user3 = new User();
        user3.setUsername("Princess Toadstool");
        user3.setEmail("Princess@yandex.ru");
        user3.setDisplayName("@ptst_toad");
        user3.setAvatarUrl("https://i.pravatar.cc/100?u=ptst_toad@pravatar.com");
        user3.setPassword(passwordEncoder.encode("Princes123@"));
        user3.setBio("my biography2 ...");
        user3.setAuthority(List.of(userAuthority));

        // Сохранение пользователей
        userRepository.saveAll(List.of(user1, user2, user3));
    }
}