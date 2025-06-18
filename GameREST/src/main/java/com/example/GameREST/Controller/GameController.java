package com.example.GameREST.Controller;


import com.example.GameREST.DTO.ReadOnlyGameDTO;
import com.example.GameREST.DTO.RequestGameDTO;
import com.example.GameREST.Entity.GameEntity;
import com.example.GameREST.Entity.GenreEntity;
import com.example.GameREST.Service.Interfaces.GameService;
import com.example.GameREST.Service.Interfaces.GenreService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("games/api")
public class GameController
{
    @Autowired
    private GameService gameService;

    @Autowired
    private GenreService genreService;

    @ModelAttribute("allGames")
    public List<GameEntity> CreateListAllGames()
    {
        return  gameService.allGames();
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

    @GetMapping("/allgames")
    public ResponseEntity<List<ReadOnlyGameDTO>> getAllGames(@ModelAttribute("allGames") List<GameEntity> allGames){

        var responseBody =  allGames
                .stream()
                .map(this::createDTO)
                .toList();

        return new ResponseEntity<>( responseBody,
                HttpStatusCode.valueOf(200));

    }



    @GetMapping("/game/{gameId}")
    public ResponseEntity<ReadOnlyGameDTO> getGame(@PathVariable("gameId") Long id)
    {
        GameEntity game = gameService.findById(id)
                .orElseThrow(()->  new IllegalArgumentException("Некорректная  игра " +
                        "или такой не существует"));

        return  new ResponseEntity<>(this.createDTO(game),HttpStatusCode.valueOf(200));
    }

    @GetMapping("/game-name")
    public ResponseEntity<ReadOnlyGameDTO> getGame(@RequestParam("name") String name)
    {
        GameEntity game = gameService.findByGameName(name)
                .orElseThrow(()->  new IllegalArgumentException("Некорректная  игра " +
                        "или такой не существует"));

        return  new ResponseEntity<>(this.createDTO(game),HttpStatusCode.valueOf(200));
    }

    @GetMapping("/search_by_genre")
    public ResponseEntity<List<ReadOnlyGameDTO>> getAllGamesByGenre(@RequestParam("genre")
                                                                        @NotBlank String genre)
    {
        var games =  gameService.findAllByGenre(genre).stream().map(this::createDTO).toList();
        return ResponseEntity
                .status(200)
                .body(games);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateGame( @PathVariable("id") Long id,
                                           @Valid @RequestBody RequestGameDTO requestGameDTO,
                                           BindingResult bindingResult) throws BindException {
        if(bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        }
        else {
            GenreEntity updateGenre = findGenre(requestGameDTO.getGenreName());
            gameService.updateGame(id, requestGameDTO.getGameName(),updateGenre);

            return ResponseEntity
                    .noContent()
                    .build();

        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addNewGame(@Valid @RequestBody RequestGameDTO requestGameDTO,
                                        BindingResult bindingResult,
                                        UriComponentsBuilder uriComponentsBuilder) throws BindException{
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
                    .created(uriComponentsBuilder.replacePath("/game/{gameName}")
                    .build(Map.of("gameName",addedGame.getGameName()))).body(addedGame);

        }




    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable("id") Long id){
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }




}
