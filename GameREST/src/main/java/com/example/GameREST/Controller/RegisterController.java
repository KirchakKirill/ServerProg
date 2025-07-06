package com.example.GameREST.Controller;


import com.example.GameREST.DTO.UserDTO;
import com.example.GameREST.Service.Interfaces.UserAuthorityService;
import com.example.GameREST.Service.Interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Создает нового пользователя с указанными учетными данными",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для регистрации",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserDTO.class),
                            examples = @ExampleObject(
                                    value = """
                    {
                      "username": "newUser",
                      "password": "securePassword123"
                    }
                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Пользователь успешно зарегистрирован"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные данные или имя пользователя уже занято",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    name = "Validation error",
                                                    value = """    
  {
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Неверные данные или формат данных",
  "instance": "/games/api/platform/add",
  "errors": [
            "Имя пользователя должно содержать от 3 до 50 символов",
            "Пароль должен содержать минимум 8 символов"
            ]
}
                                                            """
                                            ),

                                    }
                            )
                    )
            }
    )
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
                return ResponseEntity.badRequest().body("Пользователь уже существует");
            }

            userService.save(userDTO.getUsername(), userDTO.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
    }

}
