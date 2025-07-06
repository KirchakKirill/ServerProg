package com.example.GameREST.Controller;


import com.example.GameREST.CustomAnnotations.*;
import com.example.GameREST.DTO.ErrorResponseDTO;
import com.example.GameREST.DTO.PublisherDTO;
import com.example.GameREST.Entity.PublisherEntity;
import com.example.GameREST.Service.Interfaces.PublisherService;
import com.example.GameREST.Utils.ConvertToDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("games/api/publisher")
@Validated
public class PublisherController
{
    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }


    @Operation(
            summary = "Получить всех издателей с пагинацией",
            description = "Возвращает список издателей с разбивкой по страницам (по 20 элементов на странице).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный запрос",
                            content = @Content(schema = @Schema(implementation = PublisherDTO.class))

                    )
            }
    )
    @AuthApiResponse
    @ConstraintValidationExceptionResponse
    @GetMapping("all/{page}")
    public ResponseEntity<List<PublisherDTO>> getAllWithPaging(@Min(0) @Parameter(description = "Номер страницы (начиная с 0)", example = "0")
                                                                   @PathVariable("page") int page)
    {
        List<PublisherDTO> publisherDTOList =  publisherService.findAllWithPage(PageRequest.of(page,20))
                .stream()
                .map(ConvertToDTO::convertToPublisherDTO)
                .toList();

        return ResponseEntity.ok(publisherDTOList);
    }


    @Operation(
            summary = "Получить издателя по ID",
            description = "Возвращает издателя по его уникальному идентификатору.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Издатель найден",
                            content = @Content(schema = @Schema(implementation = PublisherDTO.class))),

            }
    )
    @AuthApiResponse
    @ConstraintValidationExceptionResponse
    @GetMapping("/{id}")
    public ResponseEntity<PublisherDTO> getPublisherById(@Min(1) @Parameter(description = "id издателя",example = "1")
                                                             @PathVariable("id") Long id)
    {

        PublisherEntity publisher = publisherService.findPublisherById(id)
                    .orElseThrow(()->new NoSuchElementException("Издатель не найден"));

        return ResponseEntity.ok(ConvertToDTO.convertToPublisherDTO(publisher));
    }


    @Operation(
            summary = "Создать нового издателя",
            description = "Создает нового издателя с указанным именем. Имя должно быть от 3 до 50 символов.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Издатель успешно создан",
                            content = @Content(schema = @Schema(implementation = PublisherDTO.class))),
            }
    )
    @AuthApiResponse
    @ConstraintValidationExceptionResponse
    @UniqueConstraintViolationExceptionResponse
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> createPublisher(@Parameter(description = "Название издателя,которого хотите создать")
                                                 @RequestParam @NotNull @Size(min=3,max=50) String publisher,
                                             UriComponentsBuilder uriComponentsBuilder
    )
    {
        PublisherEntity newPublisher = publisherService.create(publisher);
        return ResponseEntity
                    .created(uriComponentsBuilder.replacePath("{pubId}")
                            .build(Map.of("pubId",newPublisher.getId())))
                    .body(ConvertToDTO.convertToPublisherDTO(newPublisher));
    }


    @Operation(
            summary = "Обновить издателя",
            description = "Обновляет название издателя по его ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Издатель успешно обновлен"),
            }
    )
    @AuthApiResponse
    @ConstraintValidationExceptionResponse
    @BusinessLogicExceptionResponse
    @UniqueConstraintViolationExceptionResponse
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updatePublisher(@Min(1) @Parameter(description = "id издателя, которого хотите изменить",example = "1")
                                                 @PathVariable("id")Long id,
                                             @Parameter(description = "Новое название издателя") @RequestParam @NotNull @Size(min=3,max=50) String newPublisherName,
                                             @Parameter(description = "Обновить даже, если существуют связи")@RequestParam(value = "forceUpdate",required = false, defaultValue = "false") boolean forceUpdate)
    {

        publisherService.update(id,newPublisherName,forceUpdate);
        return ResponseEntity.noContent().build();

    }

    @Operation(
            summary = "Удалить издателя",
            description = "Удаляет издателя по его ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Издатель успешно удален"),
            }
    )
    @AuthApiResponse
    @ConstraintValidationExceptionResponse
    @BusinessLogicExceptionResponse
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteById( @Min(1) @Parameter(description = "id издателя, которого хотите удалить")
                               @PathVariable("id") Long id,
                           @Parameter(description = "Удалить даже, если существуют связи")
                           @RequestParam(value = "forceDelete",required = false,defaultValue = "false") boolean forceDelete)
    {
        publisherService.delete(id,forceDelete);
        return  ResponseEntity.noContent().build();
    }





}
