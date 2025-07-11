package com.example.GameREST.Service.RegionSalesService.Strategy.Impl;

import com.example.GameREST.DTO.RegionSalesDTO;
import com.example.GameREST.DTO.RegionSalesId;
import com.example.GameREST.Entity.GamePlatformEntity;
import com.example.GameREST.Entity.RegionEntity;
import com.example.GameREST.Entity.RegionSalesEntity;
import com.example.GameREST.Repository.RegionSalesRepository;
import com.example.GameREST.Service.RegionSalesService.Strategy.UpdateRegionSalesStrategy;
import com.example.GameREST.Service.GamePlatformService.GamePlatformService;
import com.example.GameREST.Service.RegionService.RegionService;
import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
@Component
public class UpdateRegionSalesStrategyImpl implements UpdateRegionSalesStrategy
{
    private final RegionSalesRepository regionSalesRepository;
    private final GamePlatformService gamePlatformService;
    private final RegionService regionService;

    public UpdateRegionSalesStrategyImpl(RegionSalesRepository regionSalesRepository, GamePlatformService gamePlatformService, RegionService regionService) {
        this.regionSalesRepository = regionSalesRepository;
        this.gamePlatformService = gamePlatformService;
        this.regionService = regionService;
    }

    @Override
    public RegionSalesEntity update(RegionSalesId id, RegionSalesDTO updateDTO) {
        RegionSalesEntity regionSales = regionSalesRepository.findById(id)
                .orElseThrow(()->new NoSuchElementException("Запись о региональных продажах не найдена"));

        if (id.getRegionId().equals(updateDTO.getRegionId()) &&
                id.getGamePlatformId().equals(updateDTO.getGamePlatformId())) {

            regionSales.setNumSales(updateDTO.getNumSales());
            return regionSales;
        }

        RegionSalesId newId = RegionSalesId.builder()
                .regionId(updateDTO.getRegionId())
                .gamePlatformId(updateDTO.getGamePlatformId())
                .build();

        if (regionSalesRepository.existsById(newId)) {
            throw new EntityExistsException("Такая запись Регион-Игра-Платформа уже существует");
        }

        GamePlatformEntity gamePlatform = gamePlatformService.findById(updateDTO.getGamePlatformId())
                .orElseThrow(()->new NoSuchElementException("Связь Игра - Платформа не найдена (создайте ее перед этой операцией)"));

        RegionEntity regionEntity = regionService.findById(updateDTO.getRegionId())
                .orElseThrow(() -> new NoSuchElementException("Регион не найден"));



        regionSalesRepository.delete(regionSales);
        return RegionSalesEntity.builder()
                .id(newId)
                .region(regionEntity)
                .gamePlatform(gamePlatform)
                .numSales(updateDTO.getNumSales())
                .build();
    }
}
