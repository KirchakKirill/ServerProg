package com.example.GameREST.Service.Implementations;

import com.example.GameREST.DTO.RegionSalesDTO;
import com.example.GameREST.DTO.RegionSalesId;
import com.example.GameREST.Entity.GamePlatformEntity;
import com.example.GameREST.Entity.RegionEntity;
import com.example.GameREST.Entity.RegionSalesEntity;
import com.example.GameREST.Repository.RegionSalesRepository;
import com.example.GameREST.Service.Interfaces.GamePlatformSevice;
import com.example.GameREST.Service.Interfaces.RegionSalesService;
import com.example.GameREST.Service.Interfaces.RegionService;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class RegionSalesServiceImpl implements RegionSalesService
{
    private final RegionSalesRepository regionSalesRepository;
    private final GamePlatformSevice gamePlatformSevice;
    private final RegionService regionService;

    public RegionSalesServiceImpl(RegionSalesRepository regionSalesRepository, GamePlatformSevice gamePlatformSevice, RegionService regionService) {
        this.regionSalesRepository = regionSalesRepository;
        this.gamePlatformSevice = gamePlatformSevice;
        this.regionService = regionService;
    }

    @Override
    @Transactional
    public RegionSalesEntity create(RegionSalesDTO regionSalesDTO)
    {
        RegionSalesId regionSalesId = RegionSalesId.builder()
                .regionId(regionSalesDTO.getRegionId())
                .gamePlatformId(regionSalesDTO.getGamePlatformId())
                .build();
        if (regionSalesRepository.existsById(regionSalesId))
        {
            throw  new EntityExistsException("Продажи для этой связки (регион + игра/платформа) уже существуют");
        }

        GamePlatformEntity gamePlatform = gamePlatformSevice.findById(regionSalesDTO.getGamePlatformId())
                .orElseThrow(()->new NoSuchElementException("Связь Игра - Платформа не найдена (создайте ее перед этой операцией)"));

        RegionEntity regionEntity = regionService.findById(regionSalesDTO.getRegionId())
                .orElseThrow(() -> new NoSuchElementException("Регион не найден"));;

        RegionSalesEntity regionSales = RegionSalesEntity.builder()
                .id(regionSalesId)
                .gamePlatform(gamePlatform)
                .region(regionEntity)
                .numSales(regionSalesDTO.getNumSales())
                .build();

        return regionSalesRepository.save(regionSales);
    }

    @Override
    @Transactional
    public RegionSalesEntity update(RegionSalesId id, RegionSalesDTO updateDTO) {

        RegionSalesEntity regionSales = regionSalesRepository.findById(id)
                .orElseThrow(()->new NoSuchElementException("Запись о региональных продажах не найдена"));

        if (id.getRegionId().equals(updateDTO.getRegionId()) &&
                id.getGamePlatformId().equals(updateDTO.getGamePlatformId())) {

            regionSales.setNumSales(updateDTO.getNumSales());
            return regionSalesRepository.save(regionSales);
        }

        RegionSalesId newId = RegionSalesId.builder()
                .regionId(updateDTO.getRegionId())
                .gamePlatformId(updateDTO.getGamePlatformId())
                .build();

        if (regionSalesRepository.existsById(newId)) {
            throw new EntityExistsException("Такая запись Регион-Игра-Платформа уже существует");
        }

        GamePlatformEntity gamePlatform = gamePlatformSevice.findById(updateDTO.getGamePlatformId())
                .orElseThrow(()->new NoSuchElementException("Связь Игра - Платформа не найдена (создайте ее перед этой операцией)"));

        RegionEntity regionEntity = regionService.findById(updateDTO.getRegionId())
                .orElseThrow(() -> new NoSuchElementException("Регион не найден"));

        RegionSalesEntity updated = RegionSalesEntity.builder()
                .id(newId)
                .region(regionEntity)
                .gamePlatform(gamePlatform)
                .numSales(updateDTO.getNumSales())
                .build();


        regionSalesRepository.delete(regionSales);

        return regionSalesRepository.save(updated);
    }

    @Override
    @Transactional
    public void delete(RegionSalesId id) {
        if (regionSalesRepository.existsById(id))
            regionSalesRepository.deleteById(id);
    }

    @Override
    public Page<RegionSalesEntity> findAll(Pageable pageable) {
        return regionSalesRepository.findAll(pageable);
    }

    @Override
    public Optional<RegionSalesEntity> findById(RegionSalesId id) {
        return regionSalesRepository.findById(id);
    }

}
