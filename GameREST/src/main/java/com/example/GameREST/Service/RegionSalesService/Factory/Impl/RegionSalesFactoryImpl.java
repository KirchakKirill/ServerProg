package com.example.GameREST.Service.RegionSalesService.Factory.Impl;

import com.example.GameREST.DTO.RegionSalesDTO;
import com.example.GameREST.DTO.RegionSalesId;
import com.example.GameREST.Entity.GamePlatformEntity;
import com.example.GameREST.Entity.RegionEntity;
import com.example.GameREST.Entity.RegionSalesEntity;
import com.example.GameREST.Repository.RegionSalesRepository;
import com.example.GameREST.Service.RegionSalesService.Factory.RegionSalesFactory;
import com.example.GameREST.Service.GamePlatformService.GamePlatformService;
import com.example.GameREST.Service.RegionService.RegionService;
import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class RegionSalesFactoryImpl implements RegionSalesFactory
{
    private final RegionSalesRepository regionSalesRepository;
    private final GamePlatformService gamePlatformService;
    private final RegionService regionService;

    public RegionSalesFactoryImpl(RegionSalesRepository regionSalesRepository, GamePlatformService gamePlatformService, RegionService regionService) {
        this.regionSalesRepository = regionSalesRepository;
        this.gamePlatformService = gamePlatformService;
        this.regionService = regionService;
    }

    @Override
    public RegionSalesEntity create(RegionSalesDTO regionSalesDTO)
    {
        RegionSalesId regionSalesId = createRegionSalesId(regionSalesDTO);
        existsRegionSalesById(regionSalesId);

        return   RegionSalesEntity.builder()
                .id(regionSalesId)
                .gamePlatform(findGamePlatform(regionSalesDTO.getGamePlatformId()))
                .region(findRegion(regionSalesDTO.getRegionId()))
                .numSales(regionSalesDTO.getNumSales())
                .build();
    }

    private RegionSalesId createRegionSalesId(RegionSalesDTO regionSalesDTO)
    {
        return  RegionSalesId.builder()
                .regionId(regionSalesDTO.getRegionId())
                .gamePlatformId(regionSalesDTO.getGamePlatformId())
                .build();
    }
    private void existsRegionSalesById(RegionSalesId regionSalesId)
    {
        if (regionSalesRepository.existsById(regionSalesId))
        {
            throw  new EntityExistsException("Продажи для этой связки (регион + игра/платформа) уже существуют");
        }
    }

    private GamePlatformEntity findGamePlatform(Long id) {
        return gamePlatformService.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Связь Игра - Платформа не найдена"));
    }

    private RegionEntity findRegion(Long id) {
        return regionService.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Регион не найден"));
    }
}
