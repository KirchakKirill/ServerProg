package com.example.GameREST.Controller;
import com.example.GameREST.CustomAnnotations.*;
import com.example.GameREST.DTO.ReadOnlyGameDTO;
import com.example.GameREST.DTO.RequestGameDTO;
import com.example.GameREST.Entity.GameEntity;
import com.example.GameREST.Entity.GenreEntity;
import com.example.GameREST.Service.Interfaces.GameService;
import com.example.GameREST.Service.Interfaces.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("games/api")
public class GameController
{

    private final GameService gameService;
    private  final GenreService genreService;

    public GameController(GameService gameService, GenreService genreService) {
        this.gameService = gameService;
        this.genreService = genreService;
    }

    private ReadOnlyGameDTO createDTO(GameEntity game){

        return  ReadOnlyGameDTO.builder()
                .id(game.getId())
                .gameName(game.getGameName())
                .genreName(game.getGenre() == null ? "" :game.getGenre().getGenreName())
                .build();
    }

    private GenreEntity findGenre(String genreName){
        Optional<GenreEntity> test = genreService.findGenreByName(genreName);
        GenreEntity genre;
        if (test.isPresent()) {
            genre = test.get();
        } else {
            throw new NullPointerException("Такого жанра игр не существует");
        }
        return genre;
    }


    @GetMapping("/allgames/{page}")
    @Operation(
            summary = "Получить список всех игр (пагинация)",
            security = @SecurityRequirement(name="cookieAuth")
    )
    @AuthApiResponse
    @Cacheable(value = "gamesPageCache", key = "{'withoutFilter', #page}")
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(
                            schema = @Schema(implementation = ReadOnlyGameDTO.class)
                    )
            )
    )
    @ConstraintValidationExceptionResponse
    public ResponseEntity<List<ReadOnlyGameDTO>> getAllGamesWithPaging(
            @Parameter (description = "Номер страницы (начиная с 0)", example = "0") @PathVariable("page")  int page) {

        Page<GameEntity> gamesPage = gameService.allGamesWithPaging(PageRequest.of(page, 20));
        List<ReadOnlyGameDTO> response = gamesPage.stream()
                .map(this::createDTO)
                .toList();

        return ResponseEntity.ok(response);
    }



    @GetMapping("/game/{gameId}")
    @Operation(
            summary = "Получить игру по id",
            security = @SecurityRequirement(name="cookieAuth")
    )
    @AuthApiResponse
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(
                            schema = @Schema(implementation = ReadOnlyGameDTO.class)
                    )
            )

    )
    @ConstraintValidationExceptionResponse
    public ResponseEntity<ReadOnlyGameDTO> getGame(@Parameter(description = "id игры (начиная с 1)", example = "1") @PathVariable("gameId") Long id)
    {
        GameEntity game = gameService.findById(id)
                .orElseThrow(()->  new NoSuchElementException("Некорректная  игра " +
                        "или такой не существует"));

        return  new ResponseEntity<>(this.createDTO(game),HttpStatusCode.valueOf(200));
    }

    @GetMapping("/game-name")
    @Operation(
            summary = "Получить игру по названию",
            security = @SecurityRequirement(name="cookieAuth")
    )
    @AuthApiResponse
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(
                            schema = @Schema(implementation = ReadOnlyGameDTO.class)
                    )
            )

    )
    @ConstraintValidationExceptionResponse
    public ResponseEntity<ReadOnlyGameDTO> getGame( @Parameter(description = "название игры (минимальная длина 3 символа)",example = "AIR") @RequestParam("name") String name
                                                    )
    {
        GameEntity game = gameService.findByGameName(name)
                .orElseThrow(()->  new NoSuchElementException("Некорректная  игра " +
                        "или такой не существует"));

        return  new ResponseEntity<>(this.createDTO(game),HttpStatusCode.valueOf(200));
    }

    @GetMapping("/search_by_genre")
    @Operation(
            summary = "Получить список игр по жанру",
            security = @SecurityRequirement(name="cookieAuth")
    )
    @AuthApiResponse
    @Cacheable(value = "gamesPageCache", key = "{'genre', #genre, #page}")
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(
                            schema = @Schema(implementation = ReadOnlyGameDTO.class)
                    )
            )

    )
    @ConstraintValidationExceptionResponse
    public ResponseEntity<List<ReadOnlyGameDTO>> getAllGamesByGenre( @Parameter(description = "Название жанра (минимальная длина 4 символа)",example = "Misc")
                                                                        @RequestParam("genre")
                                                                        @NotBlank String genre, @Parameter(description = "Номер страницы (начиная с нуля)", example = "0")
                                                                    @RequestParam("page") int pageNumber
    )
    {
        List<ReadOnlyGameDTO> games =  gameService.findAllByGenreWithPaging(genre,PageRequest.of(pageNumber,20))
                .stream()
                .map(this::createDTO)
                .toList();

        return ResponseEntity
                .status(200)
                .body(games);
    }


    @Operation(
            summary = "Обновить информацию об игре",
            security = @SecurityRequirement(name="cookieAuth")
    )
    @ApiResponse(
            responseCode = "203",
            description = "NO CONTENT"
    )
    @AuthApiResponse
    @BindExceptionResponse
    @UniqueConstraintViolationExceptionResponse
    @BusinessLogicExceptionResponse
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateGame( @Parameter(description = "id обновляемой игры") @PathVariable("id") Long id,
                                           @Valid @RequestBody RequestGameDTO requestGameDTO,
                                           BindingResult bindingResult,
                                         @Parameter(description = "Принудительное изменение (игнорировать связи)") @RequestParam(value = "forceUpdate",required = false,defaultValue = "false") boolean forceUpdate) throws BindException {
        if(bindingResult.hasErrors()) {

            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        }
        else {
            GenreEntity updateGenre = findGenre(requestGameDTO.getGenreName());
            gameService.updateGame(id, requestGameDTO.getGameName(),updateGenre,forceUpdate);

            return ResponseEntity
                    .noContent()
                    .build();

        }
    }

    @Operation(
            summary = "Добавить игру",
            security = @SecurityRequirement(name="cookieAuth")
    )
    @ApiResponse(
            responseCode = "201",
            description = "CREATED",
            content =
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ReadOnlyGameDTO.class)

            )
    )
    @BindExceptionResponse
    @EntityAlreadyExistsExceptionResponse
    @AuthApiResponse
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> addNewGame( @Parameter(description = "Добавляемая игра",example = "{\ngameName: Witcher 4,\ngenreName: Action\n}") @Valid @RequestBody RequestGameDTO requestGameDTO,
                                        BindingResult bindingResult,
                                        UriComponentsBuilder uriComponentsBuilder, Locale locale) throws BindException {
        if(bindingResult.hasErrors()) {

            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        }
        else{
            GenreEntity genreForAdd = findGenre(requestGameDTO.getGenreName());
            GameEntity addedGame = gameService.addNewGame(requestGameDTO.getGameName(), genreForAdd);
            return ResponseEntity
                    .created(uriComponentsBuilder.replacePath("games/api/game/{gameId}")
                    .build(Map.of("gameId",addedGame.getId()))).body(this.createDTO(addedGame));

        }

    }

    @Operation(
            summary = "Удалить игру",
            security = @SecurityRequirement(name = "cookieAuth")
    )
    @AuthApiResponse
    @BusinessLogicExceptionResponse
    @UniqueConstraintViolationExceptionResponse
    @ApiResponse(
            responseCode = "203",
            description = "NO CONTENT"
    )
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteGame(
                                               @Parameter(description = "id удаляемой игры (начиная с 1)",
                                                       example = "1")@PathVariable("id") Long id,
                                           @Parameter(description = "Принудительное удаление (игнорировать связи)") @RequestParam(value = "forceDelete",required = false,defaultValue = "false") boolean forceDelete){
        gameService.deleteGame(id,forceDelete);
        return ResponseEntity.noContent().build();
    }




}
