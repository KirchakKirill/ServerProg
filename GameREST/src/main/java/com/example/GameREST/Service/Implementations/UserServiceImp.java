package com.example.GameREST.Service.Implementations;

import com.example.GameREST.DTO.UserDTO;
import com.example.GameREST.Entity.UserAuthority;
import com.example.GameREST.Entity.UserEntity;
import com.example.GameREST.Exception.EntityAlreadyExistsException;
import com.example.GameREST.JwtAuthenticationCookie.TokenUser;
import com.example.GameREST.Repository.UserRepository;
import com.example.GameREST.Service.Interfaces.UserAuthorityService;
import com.example.GameREST.Service.Interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<UserEntity> findByUserName(String username) {
        return userRepository.findByUserName(username);
    }


    @Override
    @Transactional
    public UserEntity save(String username, String password) {

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(passwordEncoder.encode(password));
        userEntity.setAuthorities(new ArrayList<>());

        UserAuthority userAuthority = new UserAuthority();
        userAuthority.setAuthority("ROLE_USER");
        userAuthority.setUser(userEntity);

        userEntity.getAuthorities().add(userAuthority);

         return userRepository.save(userEntity);
    }

    @Override
    public boolean existsByUserName(String username) {
        return userRepository.existsByUserName(username);
    }

    @Override
    public  Optional<UserEntity> findById(Long id)
    {
        return userRepository.findById(id);
    }

    @Override
    public Optional<UserEntity> findByIdWithAuthorities(Long id) {
        return userRepository.findByIdWithAuthorities(id);
    }

    @Override
    @Transactional
    public void changeAuthority(String newAuthority, Long id) {
        UserEntity user = userRepository.findByIdWithAuthorities(id)
                .orElseThrow(()->new NoSuchElementException("Пользователь не найден"));

        long existingAuthorities = user.getAuthorities()
                .stream()
                .map(UserAuthority::getAuthority)
                .filter((a)-> a.equals("ROLE_"+newAuthority))
                .count();

        if (existingAuthorities > 0)
        {
            throw new EntityAlreadyExistsException("Такая роль уже есть у этого пользователя",
                    UserEntity.class.getSimpleName(),
                    id);
        }

        UserAuthority authority = new UserAuthority();
        authority.setAuthority("ROLE_" + newAuthority);
        authority.setUser(user);

        user.getAuthorities().add(authority);
        userRepository.save(user);

    }

    @Override
    public Page<UserEntity> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }


    @Override
    public UserEntity findCurrentUser()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TokenUser tokenUser = (TokenUser) authentication.getPrincipal();
        String userName = tokenUser.getUsername();
        log.info(userName);

        return  userRepository.findByUserName(userName).orElseThrow(()->new NoSuchElementException("Пользователь пропал"));
    }

}
