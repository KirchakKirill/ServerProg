package com.example.GameREST.Controller;

import com.example.GameREST.CustomAnnotations.AuthApiResponse;
import com.example.GameREST.CustomAnnotations.BusinessLogicExceptionResponse;
import com.example.GameREST.CustomAnnotations.ConstraintValidationExceptionResponse;
import com.example.GameREST.CustomAnnotations.UniqueConstraintViolationExceptionResponse;
import com.example.GameREST.DTO.PlatformDTO;
import com.example.GameREST.DTO.PublisherDTO;
import com.example.GameREST.Entity.PlatformEntity;
import com.example.GameREST.Entity.PublisherEntity;
import com.example.GameREST.Service.Interfaces.PlatformService;
import com.example.GameREST.Service.Interfaces.PublisherService;
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
@RequestMapping("games/api/platform")
@Validated
public class PlatformController
{
    private final PlatformService platformService;

    public PlatformController(PlatformService platformService) {
        this.platformService = platformService;
    }


    @Operation(
            summary = "Получить все платформы с пагинацией",
            description = "Возвращает список платформ с разбивкой по страницам (по 20 элементов на странице).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный запрос",
                            content = @Content(schema = @Schema(implementation = PlatformDTO.class))

                    )
            }
    )
    @AuthApiResponse
    @GetMapping("all/{page}")
    @ConstraintValidationExceptionResponse
    public ResponseEntity<List<PlatformDTO>> getAllWithPaging(@Min(0)@Parameter(description = "Номер страницы (начиная с 0)", example = "0")
                                                               @PathVariable("page") int page)
    {
        List<PlatformDTO> platformDTOList= platformService.findAllWithPaging(PageRequest.of(page,20))
                .stream()
                .map(ConvertToDTO::convertToPlatformDTO)
                .toList();

        return ResponseEntity.ok(platformDTOList);
    }


    @Operation(
            summary = "Получить платформу по ID",
            description = "Возвращает платформу по её уникальному идентификатору.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Платформа найдена",
                            content = @Content(schema = @Schema(implementation = PlatformDTO.class))),

            }
    )
    @AuthApiResponse
    @ConstraintValidationExceptionResponse
    @GetMapping("/{id}")
    public ResponseEntity<PlatformDTO> getPublisherById(@Min(1) @Parameter(description = "id платформы")
                                                            @PathVariable("id") Long id)
    {

        PlatformEntity platform = platformService.findById(id)
                .orElseThrow(()->new NoSuchElementException("Платформа не найден"));

        return ResponseEntity.ok(ConvertToDTO.convertToPlatformDTO(platform));
    }


    @Operation(
            summary = "Создать новую платформу",
            description = "Создает новую платформу с указанным именем. Имя должно быть от 3 до 50 символов.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Платформа успешна создана",
                            content = @Content(schema = @Schema(implementation = PlatformDTO.class))),
            }
    )
    @AuthApiResponse
    @ConstraintValidationExceptionResponse
    @UniqueConstraintViolationExceptionResponse
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> createPublisher(@Parameter(description = "Название платформы, которую нужно создать")
                                                 @RequestParam @NotNull @Size(min=3,max=50) String platform,
                                             UriComponentsBuilder uriComponentsBuilder
    )
    {
        PlatformEntity newPlatform = platformService.create(platform);
        return ResponseEntity
                .created(uriComponentsBuilder.replacePath("{platId}")
                        .build(Map.of("platId",newPlatform.getId())))
                .body(ConvertToDTO.convertToPlatformDTO(newPlatform));
    }


    @Operation(
            summary = "Обновить платформу",
            description = "Обновляет платформу по его ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Платформа успешно обновлена"),
            }
    )
    @AuthApiResponse
    @ConstraintValidationExceptionResponse
    @UniqueConstraintViolationExceptionResponse
    @BusinessLogicExceptionResponse
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updatePublisher(@Min(1) @Parameter(description = "id платформа, которую нужно обновить")
                                                 @PathVariable("id")Long id,
                                             @Parameter(description = "Новое название платформы")@RequestParam @NotNull @Size(min=3,max=50) String newPlatformName,
                                             @Parameter(description = "Обновить даже, если существуют связи") @RequestParam(value = "forceUpdate",required = false, defaultValue = "false") boolean forceUpdate)
    {

        platformService.update(id,newPlatformName,forceUpdate);
        return ResponseEntity.noContent().build();

    }

    @Operation(
            summary = "Удалить платформу",
            description = "Удаляет платформу по его ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Платформа успешно удалена"),
            }
    )
    @AuthApiResponse
    @ConstraintValidationExceptionResponse
    @BusinessLogicExceptionResponse
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteById(@Parameter(description = "id платформы, которую нужно удалить")
                              @Min(1) @PathVariable("id") Long id,
                           @Parameter(description = "Удалить даже, если существуют связи") @RequestParam(value = "forceDelete",required = false,defaultValue = "false") boolean forceDelete)
    {
        platformService.delete(id,forceDelete);
        return  ResponseEntity.noContent().build();
    }
}
