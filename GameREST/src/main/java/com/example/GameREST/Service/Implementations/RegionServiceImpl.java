package com.example.GameREST.Service.Implementations;

import com.example.GameREST.Entity.PublisherEntity;
import com.example.GameREST.Entity.RegionEntity;
import com.example.GameREST.Exception.BusinessLogicException;
import com.example.GameREST.Exception.UniqueConstraintViolationException;
import com.example.GameREST.Repository.RegionRepository;
import com.example.GameREST.Service.Interfaces.RegionSalesService;
import com.example.GameREST.Service.Interfaces.RegionService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    public RegionServiceImpl(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }


    @Override
    public Page<RegionEntity> findAllWithPaging(Pageable pageable) {
        return regionRepository.findAll(pageable);
    }

    @Override
    public Optional<RegionEntity> findById(Long id) {
        return regionRepository.findById(id);

    }

    @Override
    @Transactional
    public RegionEntity create(String regionName) {

        RegionEntity existing = regionRepository.findByRegionName(regionName).orElse(null);
        if (existing != null) {
            throw new UniqueConstraintViolationException(
                    "Вы пытаетесь добавить регион, игнорируя ограничение уникальности названия",
                    RegionEntity.class.getSimpleName(),
                    existing.getId(),
                    regionName);
        }
        return regionRepository.save(RegionEntity.builder()
                .regionName(regionName)
                .build());
    }

    @Override
    @Transactional
    public void update(Long id, String regionName,boolean forceUpdate) {

        RegionEntity region = regionRepository.findById(id)
                .orElseThrow(()->new NoSuchElementException("Регион не найден"));

        Integer count = regionRepository.existsRegionByRegionSales(region);

        if (count > 0 && !forceUpdate) {
            throw new BusinessLogicException("Регион имеет " + count + " связей типа [Регион - Игра - Платформа]." +
                    "Убедитесь, что изменение данной сущности не повлияет на работу вашего приложения." +
                    "Если уверенны, то forceUpdate = true");
        }

        RegionEntity existing = regionRepository.findByRegionName(regionName).orElse(null);
        if (existing == null)
        {
            region.setRegionName(regionName);
            regionRepository.save(region);
        }
        else
        {
            throw new UniqueConstraintViolationException("Регион с таким названием уже существует",
                    RegionEntity.class.getSimpleName(),
                    existing.getId(),
                    regionName);
        }
    }

    @Override
    @Transactional
    public void delete(Long id, boolean forceDelete)
    {

        RegionEntity region = regionRepository.findById(id)
                .orElseThrow(()->new NoSuchElementException("Регион не найден"));

        Integer count = regionRepository.existsRegionByRegionSales(region);

        if (count > 0 && !forceDelete) {
            throw new BusinessLogicException("Регион имеет " + count + " связей типа [Регион - Игра - Платформа]." +
                    "Убедитесь, что удаление данной сущности не повлияет на работу вашего приложения." +
                    "Если уверенны, то forceDelete = true");
        }
        regionRepository.delete(region);

    }

    @Override
    public Optional<RegionEntity> findByRegionName(String regionName) {
        return regionRepository.findByRegionName(regionName);
    }
}
