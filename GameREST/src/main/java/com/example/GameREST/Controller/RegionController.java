package com.example.GameREST.Controller;

import com.example.GameREST.CustomAnnotations.*;
import com.example.GameREST.DTO.RegionDTO;
import com.example.GameREST.Entity.RegionEntity;
import com.example.GameREST.Service.GeneraLogic.CreateData.CreationPolicyState;
import com.example.GameREST.Service.RegionService.RegionService;
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
@RequestMapping("games/api/region")
@Validated
public class RegionController {
    private final RegionService regionService;
    private final CreationPolicyState POLICY_STATE = CreationPolicyState.STRICT;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @Operation(
            summary = "Получить все регионы с пагинацией",
            description = "Возвращает список регионов с разбивкой по страницам (по 20 элементов на странице).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный запрос",
                            content = @Content(schema = @Schema(implementation = RegionDTO.class))
                    )
            }
    )
    @AuthApiResponse
    @ConstraintValidationExceptionResponse
    @GetMapping("all/{page}")
    public ResponseEntity<List<RegionDTO>> getAllWithPaging(@Min(0)@Parameter(description = "Номер страницы (начиная с 0)", example = "0")
                                                            @PathVariable("page") int page) {
        List<RegionDTO> regionDTOList = regionService.findAllWithPaging(PageRequest.of(page, 20))
                .stream()
                .map(ConvertToDTO::convertToRegionDTO)
                .toList();

        return ResponseEntity.ok(regionDTOList);
    }

    @Operation(
            summary = "Получить регион по ID",
            description = "Возвращает регион по его уникальному идентификатору.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Регион найден",
                            content = @Content(schema = @Schema(implementation = RegionDTO.class))),
            }
    )
    @AuthApiResponse
    @EntityAlreadyExistsExceptionResponse
    @GetMapping("/{id}")
    public ResponseEntity<RegionDTO> getRegionById(@Min(1) @Parameter(description = "id региона")
                                                   @PathVariable("id") Long id) {
        RegionEntity region = regionService.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Регион не найден"));

        return ResponseEntity.ok(ConvertToDTO.convertToRegionDTO(region));
    }

    @Operation(
            summary = "Создать новый регион",
            description = "Создает новый регион с указанным именем. Имя должно быть от 3 до 50 символов.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Регион успешно создан",
                            content = @Content(schema = @Schema(implementation = RegionDTO.class))),
            }
    )
    @AuthApiResponse
    @ConstraintValidationExceptionResponse
    @UniqueConstraintViolationExceptionResponse
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> createRegion(@Parameter(description = "Название региона, который нужно создать")
                                          @RequestParam @NotNull @Size(min = 3, max = 50) String regionName,
                                          UriComponentsBuilder uriComponentsBuilder) {
        RegionEntity newRegion = regionService.create(regionName,POLICY_STATE);
        return ResponseEntity
                .created(uriComponentsBuilder.replacePath("{regionId}")
                        .build(Map.of("regionId", newRegion.getId())))
                .body(ConvertToDTO.convertToRegionDTO(newRegion));
    }

    @Operation(
            summary = "Обновить регион",
            description = "Обновляет регион по его ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Регион успешно обновлен"),
            }
    )
    @AuthApiResponse
    @ConstraintValidationExceptionResponse
    @BusinessLogicExceptionResponse
    @UniqueConstraintViolationExceptionResponse
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateRegion( @Min(1) @Parameter(description = "id региона, который нужно обновить")
                                          @PathVariable("id") Long id,
                                          @Parameter(description = "Новое название жанра") @RequestParam @NotNull @Size(min = 3, max = 50) String newRegionName,
                                          @Parameter(description = "Обновить даже, если существуют связи")@RequestParam(value = "forceUpdate", required = false, defaultValue = "false") boolean forceUpdate) {
        regionService.update(id, newRegionName, forceUpdate);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Удалить регион",
            description = "Удаляет регион по его ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Регион успешно удален"),
            }
    )
    @AuthApiResponse
    @ConstraintValidationExceptionResponse
    @BusinessLogicExceptionResponse
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteById(@Min(1) @Parameter(description = "id региона, который нужно удалить")
                           @PathVariable("id") Long id,
                           @Parameter(description = "Удалить даже, если существуют связи")@RequestParam(value = "forceDelete", required = false, defaultValue = "false") boolean forceDelete) {
        regionService.delete(id, forceDelete);
        return  ResponseEntity.noContent().build();
    }
}