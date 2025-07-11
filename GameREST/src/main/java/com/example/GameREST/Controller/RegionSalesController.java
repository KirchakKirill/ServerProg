package com.example.GameREST.Controller;

import com.example.GameREST.CustomAnnotations.AuthApiResponse;
import com.example.GameREST.CustomAnnotations.BindExceptionResponse;
import com.example.GameREST.CustomAnnotations.ConstraintValidationExceptionResponse;
import com.example.GameREST.CustomAnnotations.EntityExistsExceptionResponse;
import com.example.GameREST.DTO.GetRequestRegionSalesDTO;
import com.example.GameREST.DTO.GetUpdateRequestRegionSalesDTO;
import com.example.GameREST.DTO.RegionSalesDTO;
import com.example.GameREST.DTO.RegionSalesId;
import com.example.GameREST.Entity.RegionSalesEntity;
import com.example.GameREST.Service.RegionSalesService.RegionSalesService;
import com.example.GameREST.Utils.ConvertToDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
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

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("games/api/region-sales")
public class RegionSalesController {

    private final RegionSalesService regionSalesService;

    public RegionSalesController(RegionSalesService regionSalesService) {
        this.regionSalesService = regionSalesService;
    }


    @Operation(
            summary = "Получить список региональных продаж",
            security = @SecurityRequirement(name="cookieAuth"),
            description = "Возвращает информацию о региональных продажах игр на платформах."
    )
    @AuthApiResponse
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(
                            schema = @Schema(implementation = RegionSalesDTO.class)
                    )
            )
    )
    @GetMapping("/all/{page}")
    @ConstraintValidationExceptionResponse
    @Cacheable(value = "regionSalesCache", key = "{'withoutFilter', #page}")
    public ResponseEntity<List<RegionSalesDTO>> getAll(
            @Parameter(description = "нужная страница (начиная с нуля)", example = "0")
            @PathVariable(name = "page") int page) {

        List<RegionSalesDTO> regionSalesList = regionSalesService.findAll(PageRequest.of(page, 20))
                .stream()
                .map(ConvertToDTO::convertToRegionSalesDTO)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(regionSalesList);
    }

    @Operation(
            summary = "Получить информацию о региональных продажах по ID",
            security = @SecurityRequirement(name="cookieAuth"),
            description = "Возвращает информацию о региональных продажах по идентификаторам региона и связки игра-платформа."
    )
    @AuthApiResponse
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RegionSalesDTO.class)
            )

    )
    @GetMapping("/getById/{gpId}/{regionId}")
    public ResponseEntity<RegionSalesDTO> getRegionSales( @Parameter(description = "id связи Игра-Платформа")
                                                             @PathVariable("gpId") Long gpId,
                                                          @Parameter(description = "id региона")
                                                          @PathVariable("regionId") Long regionId) {

        RegionSalesEntity regionSalesEntity = regionSalesService.findById(
                        new RegionSalesId(regionId, gpId))
                .orElseThrow(() -> new NoSuchElementException("Не найден элемент"));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ConvertToDTO.convertToRegionSalesDTO(regionSalesEntity));
    }

    @Operation(
            summary = "Добавить информацию о региональных продажах",
            security = @SecurityRequirement(name="cookieAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания записи о региональных продажах",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RegionSalesDTO.class)
                    )
            )
    )
    @AuthApiResponse
    @ApiResponse(
            responseCode = "201",
            description = "CREATED",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RegionSalesDTO.class)
            )
    )
    @BindExceptionResponse
    @EntityExistsExceptionResponse
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> addRegionSalesInfo(
            @Parameter(description = "Данные о региональных продажах")
            @Valid @RequestBody RegionSalesDTO regionSalesDTO,
            BindingResult bindingResult,
            UriComponentsBuilder uriComponentsBuilder) throws BindException {

        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            RegionSalesEntity regionSalesEntity = regionSalesService.create(regionSalesDTO);
            return ResponseEntity.created(uriComponentsBuilder.replacePath("/region-sales/findById")
                            .build(Map.of(
                                    "regionId", regionSalesEntity.getId().getRegionId(),
                                    "gamePlatformId", regionSalesEntity.getId().getGamePlatformId())))
                    .body(ConvertToDTO.convertToRegionSalesDTO(regionSalesEntity));
        }
    }

    @Operation(
            summary = "Обновить информацию о региональных продажах",
            security = @SecurityRequirement(name="cookieAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для обновления записи о региональных продажах",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GetUpdateRequestRegionSalesDTO.class)
                    )
            )
    )
    @AuthApiResponse
    @ApiResponse(
            responseCode = "204",
            description = "NO CONTENT"
    )
    @BindExceptionResponse
    @EntityExistsExceptionResponse
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateRegionSalesInfo(
            @Parameter(description = "Обновляемая запись [id региона и связи Игра - Платформа] + данные для обновления") @Valid @RequestBody GetUpdateRequestRegionSalesDTO getUpdateRequestRegionSalesDTO,
            BindingResult bindingResult) throws BindException {

        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            RegionSalesId id = new RegionSalesId(getUpdateRequestRegionSalesDTO.getOldRegionId(),getUpdateRequestRegionSalesDTO.getOldGamePlatformId());
            regionSalesService.update(id, new RegionSalesDTO(getUpdateRequestRegionSalesDTO.getNewRegionId(),
                    getUpdateRequestRegionSalesDTO.getNewGamePlatformId(),
                    getUpdateRequestRegionSalesDTO.getNumSales()));
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @Operation(
            summary = "Удалить информацию о региональных продажах",
            security = @SecurityRequirement(name = "cookieAuth")
    )
    @AuthApiResponse
    @ApiResponse(
            responseCode = "204",
            description = "NO CONTENT"
    )
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @BindExceptionResponse
    public ResponseEntity<Void> deleteRegionSalesInfo(
           @Parameter(description = "Удаляемая запись [id региона и связи Игра - Платформа]")
           @Valid @RequestBody GetRequestRegionSalesDTO getRequestRegionSalesDTO) {

        regionSalesService.delete(new RegionSalesId(getRequestRegionSalesDTO.getRegionId(), getRequestRegionSalesDTO.getGamePlatformId()));
        return ResponseEntity.noContent().build();
    }
}