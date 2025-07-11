package com.example.GameREST.Controller;

import com.example.GameREST.CustomAnnotations.AuthApiResponse;
import com.example.GameREST.DTO.ReadOnlyUserDTO;
import com.example.GameREST.Entity.UserEntity;
import com.example.GameREST.Service.UserService.UserService;
import com.example.GameREST.Utils.ConvertToDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("games/api/change-authority")
public class AuthorityController
{
    private final UserService userService;

    public AuthorityController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Изменение прав пользователя",
            description = "Доступно только для ADMIN",
            security = @SecurityRequirement(name="cookieAuth"),
            parameters = {
                    @Parameter(name = "id", description = "ID пользователя", in = ParameterIn.PATH),
                    @Parameter(name = "newAuthority", description = "Новая роль (например, ADMIN, MODERATOR)", in = ParameterIn.QUERY)
            }
    )
    @ApiResponse(responseCode = "204", description = "Права успешно изменены")
    @AuthApiResponse
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> changeUserAuthority(@PathVariable("id") Long id,
                                                    @RequestParam String newAuthority)
    {
        userService.changeAuthority(newAuthority,id);

        return  ResponseEntity.noContent().build();
    }


    @Operation(
            summary = "Получить список пользователей (пагинация)",
            description = "Доступно только для ADMIN",
            security = @SecurityRequirement(name="cookieAuth"),
            parameters = {
                    @Parameter(name = "page", description = "Номер страницы (начиная с 0)", in = ParameterIn.PATH)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список пользователей",
                            content = @Content(schema = @Schema(implementation = ReadOnlyUserDTO.class))

                    )
            }
    )
    @GetMapping("/users/{page}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<ReadOnlyUserDTO>> getAllUsers(@PathVariable("page") int page)
    {
        List<ReadOnlyUserDTO> userDTOList = userService.findAllUsers(PageRequest.of(page,10))
                .stream()
                .map(ConvertToDTO::convertToReadOnlyUserDTO)
                .toList();
        return ResponseEntity.ok(userDTOList);
    }


    @Operation(
            summary = "Получить пользователя по ID",
            security = @SecurityRequirement(name="cookieAuth"),
            description = "Доступно только для ADMIN"
    )
    @AuthApiResponse
    @ApiResponse(
            responseCode = "200",
            description = "Данные пользователя",
            content = @Content(schema = @Schema(implementation = ReadOnlyUserDTO.class))
    )
    @GetMapping("/user/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ReadOnlyUserDTO> getAllUsers(@PathVariable("id") Long id)
    {
       UserEntity user =  userService.findById(id)
                .orElseThrow(()->new NoSuchElementException("Пользователь не найден"));

        return ResponseEntity.ok(ConvertToDTO.convertToReadOnlyUserDTO(user));
    }


    @Operation(
            summary = "Получить текущего аутентифицированного пользователя",
            security = @SecurityRequirement(name="cookieAuth"),
            description = "Доступно только для ADMIN",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Данные текущего пользователя",
                            content = @Content(schema = @Schema(implementation = ReadOnlyUserDTO.class))
                    )
            }
    )
    @GetMapping("/current-user")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ReadOnlyUserDTO> getCurrentUser()
    {
        return  ResponseEntity.ok(ConvertToDTO.convertToReadOnlyUserDTO(userService.findCurrentUser()));
    }


}
