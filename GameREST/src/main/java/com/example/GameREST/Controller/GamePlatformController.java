package com.example.GameREST.Controller;

import com.example.GameREST.DTO.ReadOnlyGamePlatformDTO;
import com.example.GameREST.DTO.RequestGamePlatformDTO;
import com.example.GameREST.Entity.GamePlatformEntity;
import com.example.GameREST.Entity.GamePublisherEntity;
import com.example.GameREST.Service.Interfaces.GamePlatformSevice;
import com.example.GameREST.Service.Interfaces.GamePublisherService;
import com.example.GameREST.Service.Interfaces.GameService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("games/api/platform")
public class GamePlatformController {

    @Autowired
    private GamePlatformSevice gamePlatformSevice;

    @Autowired
    private GameService gameService;

    @Autowired
    private GamePublisherService gamePublisherService;

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


    @GetMapping("/game-platform/all")
    public ResponseEntity<List<ReadOnlyGamePlatformDTO>> getAll()
    {

        List<ReadOnlyGamePlatformDTO> gamePlatformEntityList = gamePlatformSevice.getAll().stream().map(this::convertToReadOnlyGamePlatformDTO).toList();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gamePlatformEntityList);

    }



    @GetMapping("game-platform/{id}")
    public ResponseEntity<ReadOnlyGamePlatformDTO> getGamePlatform(@PathVariable("id") Long id)
    {
        GamePlatformEntity gamePlatformEntity = gamePlatformSevice.findById(id)
                .orElseThrow(() -> new DataIntegrityViolationException("Не найден элемент"));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.convertToReadOnlyGamePlatformDTO(gamePlatformEntity));
    }



    @PostMapping("/add")
    public ResponseEntity<?> addGamePlatformInfo(@Valid @RequestBody RequestGamePlatformDTO requestGamePlatformDTO,
                                                 BindingResult bindingResult,
                                                 UriComponentsBuilder uriComponentsBuilder) throws BindException

    {
        if (bindingResult.hasErrors())
        {
            if (bindingResult instanceof BindException exception) throw  exception;
            else throw  new BindException(bindingResult);
        }
        else {
            GamePlatformEntity gamePlatformEntity = gamePlatformSevice.save(requestGamePlatformDTO);
            return ResponseEntity.created(uriComponentsBuilder.replacePath("/game-platform/{id}")
                    .build(Map.of("id",gamePlatformEntity.getId()))).body(this.convertToReadOnlyGamePlatformDTO(gamePlatformEntity));
        }



    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateGamePlatformInfo(@PathVariable(value = "id") Long id, @Valid @RequestBody RequestGamePlatformDTO requestGamePlatformDTO,
                                                    BindingResult bindingResult) throws BindException
    {
        if (bindingResult.hasErrors())
        {
            if (bindingResult instanceof BindException exception) throw  exception;
            else throw  new BindException(bindingResult);
        }
        else {
            Optional<GamePlatformEntity>  gamePlatformEntity = gamePlatformSevice.findById(id);
            GamePlatformEntity gamePlatform;
            if (gamePlatformEntity.isPresent())
            {
                gamePlatform = gamePlatformEntity.get();
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Не найден объект с таким id");
            }

            try{

                gamePlatformSevice.update(gamePlatform,requestGamePlatformDTO);

            } catch (IllegalArgumentException exception) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("при попытки изменения данных возникала ошибка: " + exception.getMessage() );
            }
            catch (ResponseStatusException exception){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("детали: " + exception.getMessage() +"\n" +"причина: " + exception.getReason());
            }

            return ResponseEntity.ok(this.convertToReadOnlyGamePlatformDTO(gamePlatform));
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteGamePlatformInfoById(@PathVariable("id") Long id)
    {
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }



}
