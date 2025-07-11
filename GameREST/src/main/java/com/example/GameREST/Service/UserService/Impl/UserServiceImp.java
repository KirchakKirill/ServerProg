package com.example.GameREST.Service.UserService.Impl;

import com.example.GameREST.DTO.UserDTO;
import com.example.GameREST.Entity.UserAuthority;
import com.example.GameREST.Entity.UserEntity;
import com.example.GameREST.JwtAuthenticationCookie.TokenUser;
import com.example.GameREST.Repository.UserRepository;
import com.example.GameREST.Service.UserService.Factories.UserAuthorityFactory;
import com.example.GameREST.Service.UserService.Factories.UserFactory;
import com.example.GameREST.Service.UserService.UserService;
import com.example.GameREST.Service.UserService.Validator.UserAuthorityValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserFactory userFactory;
    private final UserAuthorityFactory userAuthorityFactory;
    private final UserAuthorityValidator userAuthorityValidator;


    public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder, UserFactory userFactory, UserAuthorityFactory userAuthorityFactory, UserAuthorityValidator userAuthorityValidator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userFactory = userFactory;
        this.userAuthorityFactory = userAuthorityFactory;
        this.userAuthorityValidator = userAuthorityValidator;
    }

    @Override
    public Optional<UserEntity> findByUserName(String username) {
        return userRepository.findByUserName(username);
    }


    @Override
    @Transactional
    public UserEntity save(UserDTO userDTO) {

        UserEntity user = userFactory.create(userDTO,passwordEncoder);
        UserAuthority authority = userAuthorityFactory.create(user,null);
        user.getAuthorities().add(authority);
        return userRepository.save(user);
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

        userAuthorityValidator.validate(user,newAuthority);
        UserAuthority authority = userAuthorityFactory.create(user,newAuthority);

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
