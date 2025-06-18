package com.example.GameREST.Controller;


import com.example.GameREST.DTO.UserDTO;
import com.example.GameREST.Service.Interfaces.UserAuthorityService;
import com.example.GameREST.Service.Interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("games/api")
public class RegisterController {


    private final UserService userService;


    public RegisterController(UserService userService, UserAuthorityService userAuthorityService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {

        if (userService.existsByUserName(userDTO.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        var user = userService.save(userDTO.getUsername(), userDTO.getPassword());
        log.info(String.valueOf(user));

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

}
