package com.example.Greenswamp.Services;


import com.example.Greenswamp.Data.UserPrincipal;
import com.example.Greenswamp.Data.UserData;
import com.example.Greenswamp.Entity.Authority;
import com.example.Greenswamp.Entity.User;
import com.example.Greenswamp.Repository.AuthorityRepository;
import com.example.Greenswamp.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements  UserService{

    private final UserRepository userRepository;

    private  final AuthorityRepository authorityRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImp(UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUser();
    }

    @Override
    public Optional<User> findUserById(Long userId) {
        return userRepository.findUserById(userId);
    }

    @Override
    @Transactional
    public void saveUser(UserData user) {

        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setDisplayName(user.getDisplayName());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setEmail(user.getEmail());


        Authority authority = authorityRepository.findByAuthorityType(Authority.AuthorityType.USER);
        newUser.setAuthority(new ArrayList<>());
        newUser.getAuthority().add(authority);

        userRepository.save(newUser);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =  userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return new UserPrincipal(user.getUsername(),user.getPassword(),user.getAuthority());

    }
}
