package com.example.GameREST.Controller;

import com.example.GameREST.CustomAnnotations.AuthApiResponse;
import com.example.GameREST.CustomAnnotations.BindExceptionResponse;
import com.example.GameREST.CustomAnnotations.ConstraintValidationExceptionResponse;
import com.example.GameREST.CustomAnnotations.EntityAlreadyExistsExceptionResponse;
import com.example.GameREST.DTO.ReadOnlyGamePlatformDTO;
import com.example.GameREST.DTO.RequestGamePlatformDTO;
import com.example.GameREST.DTO.RequestGamePlatformUpdateDTO;
import com.example.GameREST.Entity.GamePlatformEntity;
import com.example.GameREST.Service.Interfaces.GamePlatformSevice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@RestController
@RequestMapping("games/api/game-platform")
public class GamePlatformController {


    private final GamePlatformSevice gamePlatformSevice;

    public GamePlatformController(GamePlatformSevice gamePlatformSevice) {
        this.gamePlatformSevice = gamePlatformSevice;

    }

    private ReadOnlyGamePlatformDTO  convertToReadOnlyGamePlatformDTO(GamePlatformEntity gamePlatformEntity)
    {
        return ReadOnlyGamePlatformDTO.builder()
                .id(gamePlatformEntity.getId())
                .platformName(gamePlatformEntity.getPlatform().getPlatformName())
                .publisherName(gamePlatformEntity.getGamePublisher().getPublisher().getPublisherName())
                .gameName(gamePlatformEntity.getGamePublisher().getGame().getGameName())
                .releaseYear(gamePlatformEntity.getReleaseYear())
                .genreName(gamePlatformEntity.getGamePublisher().getGame().getGenre().getGenreName())
                .build();
    }

    @Operation(
            summary = "Получить список  связей  Игра-Платформа",
            security = @SecurityRequirement(name="cookieAuth"),
            description = "Возвращает связи между играми, платформами и издателями. " +
                    "Каждая запись представляет собой сущность GamePlatform из БД."
    )
    @AuthApiResponse
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(
                            schema = @Schema(implementation = ReadOnlyGamePlatformDTO.class)
                    )
            )
    )
    @GetMapping("/all/{page}")
    @Cacheable(value = "gamePlatformCache", key = "{'withoutFilter', #page}")
    public ResponseEntity<List<ReadOnlyGamePlatformDTO>> getAll( @Parameter (description = "нужная страница (начиная с нуля)", example = "0")
                                                                    @PathVariable(name = "page") int page)
    {

        List<ReadOnlyGamePlatformDTO> gamePlatformEntityList = gamePlatformSevice.getAll(PageRequest.of(page,20))
                .stream()
                .map(this::convertToReadOnlyGamePlatformDTO)
                .toList();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gamePlatformEntityList);

    }



    @Operation(
            summary = "Получить связь  Игра-Платформа по id",
            security = @SecurityRequirement(name="cookieAuth"),
            description = "Возвращает связь между игрой, платформой и издателем по id" +
                    "Запись представляет собой сущность GamePlatform из БД."
    )
    @AuthApiResponse
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(
                            schema = @Schema(implementation = ReadOnlyGamePlatformDTO.class)
                    )
            )
    )
    @GetMapping("/{id}")
    public ResponseEntity<ReadOnlyGamePlatformDTO> getGamePlatform(@Parameter(description = "id запрашиваемой связи (начиная с 1)",example = "1")
                                                                       @PathVariable("id") Long id)
    {
        GamePlatformEntity gamePlatformEntity = gamePlatformSevice.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Не найден элемент"));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.convertToReadOnlyGamePlatformDTO(gamePlatformEntity));
    }


    @Operation(
            summary = "Добавить новую связь Игра-Платформа",
            security = @SecurityRequirement(name="cookieAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания связи Игра-Платформа",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RequestGamePlatformDTO.class)
                    )
            )

    )
    @AuthApiResponse
    @ApiResponse(

            responseCode = "201",
            description = "CREATED",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ReadOnlyGamePlatformDTO.class)

            )
    )
    @BindExceptionResponse
    @EntityAlreadyExistsExceptionResponse
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> addGamePlatformInfo(@Parameter(description = "Создаваемая связь") @Valid @RequestBody RequestGamePlatformDTO requestGamePlatformDTO,
                                                 BindingResult bindingResult,
                                                 UriComponentsBuilder uriComponentsBuilder,Locale locale) throws BindException

    {
        if (bindingResult.hasErrors())
        {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        }
        else {
            GamePlatformEntity gamePlatformEntity = gamePlatformSevice.save(requestGamePlatformDTO);
            return ResponseEntity.created(uriComponentsBuilder.replacePath("games/api/game-platform/{id}")
                    .build(Map.of("id",gamePlatformEntity.getId()))).body(this.convertToReadOnlyGamePlatformDTO(gamePlatformEntity));
        }

    }


    @Operation(
            summary = "Обновить связь Игра-Платформа",
            security = @SecurityRequirement(name="cookieAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для обновления связи Игра-Платформа",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RequestGamePlatformUpdateDTO.class)
                    )
            )

    )
    @AuthApiResponse
    @ApiResponse(

            responseCode = "204",
            description = "NO CONTENT"
    )
    @BindExceptionResponse
    @EntityAlreadyExistsExceptionResponse
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateGamePlatformInfo(@Parameter(description = "Обновляемая сущность") @PathVariable(value = "id") Long id,
                                                    @Parameter(description = "Данные для обновления сущности") @Valid @RequestBody RequestGamePlatformUpdateDTO updateDTO,
                                                    BindingResult bindingResult, Locale locale) throws BindException
    {
        if (bindingResult.hasErrors())
        {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        }
        else {

            try{

                gamePlatformSevice.update(id,updateDTO);

            } catch (NoSuchElementException exception) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("при попытки изменения данных возникала ошибка: " + exception.getMessage());
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @Operation(
            summary = "Удалить связь Игра-Платформа",
            security = @SecurityRequirement(name = "cookieAuth")
    )
    @AuthApiResponse
    @ApiResponse(
            responseCode = "204",
            description = "NO CONTENT"
    )
    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteGamePlatformInfoById(@Parameter(description = "id удаляемой сущности")
                                                               @PathVariable("id") Long id)
    {
        gamePlatformSevice.delete(id);
        return ResponseEntity.noContent().build();
    }



}
