package com.example.GameREST.Controller;

import com.example.GameREST.CustomAnnotations.AuthApiResponse;
import com.example.GameREST.CustomAnnotations.BusinessLogicExceptionResponse;
import com.example.GameREST.CustomAnnotations.ConstraintValidationExceptionResponse;
import com.example.GameREST.CustomAnnotations.UniqueConstraintViolationExceptionResponse;
import com.example.GameREST.DTO.GenreDTO;
import com.example.GameREST.Entity.GenreEntity;
import com.example.GameREST.Entity.PlatformEntity;
import com.example.GameREST.Service.Interfaces.GenreService;
import com.example.GameREST.Utils.ConvertToDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("games/api/genre")
@Validated
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @Operation(
            summary = "Получить все жанры с пагинацией",
            description = "Возвращает список жанров с разбивкой по страницам (по 20 элементов на странице).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный запрос",
                            content = @Content(schema = @Schema(implementation = GenreDTO.class))

                    )
            }
    )
    @AuthApiResponse
    @GetMapping("/all/{page}")
    @ConstraintValidationExceptionResponse
    public ResponseEntity<List<GenreDTO>> getAllWithPaging(
            @Parameter(description = "Номер страницы (начиная с 0)", example = "0")
            @NotNull @Min(value = 0) @PathVariable("page") int page) {

        List<GenreDTO> genreDTOList = genreService.findAllWithPaging(PageRequest.of(page,20))
                .stream()
                .map(ConvertToDTO::convertToGenreDTO)
                .toList();

        return ResponseEntity.ok(genreDTOList);
    }

    @Operation(
            summary = "Получить жанр по ID",
            description = "Возвращает жанр по его уникальному идентификатору.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Жанр найден",
                            content = @Content(schema = @Schema(implementation = GenreDTO.class))),
            }
    )
    @AuthApiResponse
    @ConstraintValidationExceptionResponse
    @GetMapping("/{id}")
    public ResponseEntity<GenreDTO> getById(
            @Parameter(description = "ID жанра", example = "1")
            @NotNull @Min(value = 1) @PathVariable("id") Long id) {

        GenreEntity genre = genreService.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Жанр не найден"));

        return ResponseEntity.ok(ConvertToDTO.convertToGenreDTO(genre));
    }

    @Operation(
            summary = "Создать новый жанр",
            description = "Создает новый жанр с указанным названием. Название должно быть от 3 до 50 символов.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Жанр успешно создан",
                            content = @Content(schema = @Schema(implementation = GenreDTO.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные параметры запроса")
            }
    )
    @AuthApiResponse
    @ConstraintValidationExceptionResponse
    @UniqueConstraintViolationExceptionResponse
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> createGenre(
            @Parameter(description = "Название жанра (3-50 символов)", example = "Ролевые игры")
            @RequestParam("genreName") @Size(min = 3, max = 50) String genreName,
            UriComponentsBuilder uriComponentsBuilder) {

        GenreEntity newGenre = genreService.create(genreName);
        return ResponseEntity
                .created(uriComponentsBuilder.replacePath("{genreId}")
                        .build(Map.of("genreId", newGenre.getId())))
                .body(ConvertToDTO.convertToGenreDTO(newGenre));
    }

    @Operation(
            summary = "Обновить жанр",
            description = "Обновляет название жанра по его ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Жанр успешно обновлен"),
            }
    )
    @AuthApiResponse
    @ConstraintValidationExceptionResponse
    @BusinessLogicExceptionResponse
    @UniqueConstraintViolationExceptionResponse
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> updateGenre(
            @Min(1) @Parameter(description = "ID жанра", example = "1")
            @PathVariable("id") Long id,
            @Parameter(description = "Новое название жанра (3-50 символов)", example = "Экшен")
            @RequestParam("genreName") @Size(min = 3, max = 50) String newGenreName,
            @Parameter(description = "Принудительное обновление (игнорировать связи)", example = "false")
            @RequestParam(value = "forceUpdate", required = false, defaultValue = "false") boolean forceUpdate) {

        genreService.update(id, newGenreName, forceUpdate);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Удалить жанр",
            description = "Удаляет жанр по его ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Жанр успешно удален"),
            }
    )
    @AuthApiResponse
    @BusinessLogicExceptionResponse
    @ConstraintValidationExceptionResponse
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteGenre(
            @Parameter(description = "ID жанра", example = "1")
            @Min(1) @PathVariable("id") Long id,
            @Parameter(description = "Принудительное удаление (игнорировать связи)", example = "false")
            @RequestParam(value = "forceDelete", required = false, defaultValue = "false") boolean forceDelete) {

        genreService.delete(id, forceDelete);
        return ResponseEntity.noContent().build();
    }
}