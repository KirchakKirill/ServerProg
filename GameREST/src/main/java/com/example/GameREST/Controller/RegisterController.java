package com.example.GameREST.Controller;


import com.example.GameREST.DTO.UserDTO;
import com.example.GameREST.Service.Interfaces.UserAuthorityService;
import com.example.GameREST.Service.Interfaces.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
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
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) throws  BindException {

        if(bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        }
        else {

            if (userService.existsByUserName(userDTO.getUsername())) {
                return ResponseEntity.badRequest().body("Username already exists");
            }

            userService.save(userDTO.getUsername(), userDTO.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }







    }

}
