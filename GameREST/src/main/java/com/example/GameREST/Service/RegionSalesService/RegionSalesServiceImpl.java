package com.example.GameREST.Service.RegionSalesService;

import com.example.GameREST.DTO.RegionSalesDTO;
import com.example.GameREST.DTO.RegionSalesId;
import com.example.GameREST.Entity.RegionSalesEntity;
import com.example.GameREST.Repository.RegionSalesRepository;
import com.example.GameREST.Service.RegionSalesService.Factory.RegionSalesFactory;
import com.example.GameREST.Service.RegionSalesService.Strategy.UpdateRegionSalesStrategy;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegionSalesServiceImpl implements RegionSalesService
{
    private final RegionSalesRepository regionSalesRepository;
    private final RegionSalesFactory regionSalesFactory;
    private final UpdateRegionSalesStrategy updateRegionSalesStrategy;

    public RegionSalesServiceImpl(RegionSalesRepository regionSalesRepository, RegionSalesFactory regionSalesFactory, UpdateRegionSalesStrategy updateRegionSalesStrategy) {
        this.regionSalesRepository = regionSalesRepository;
        this.regionSalesFactory = regionSalesFactory;
        this.updateRegionSalesStrategy = updateRegionSalesStrategy;
    }

    @Override
    @Transactional
    public RegionSalesEntity create(RegionSalesDTO regionSalesDTO)
    {
        return regionSalesRepository.save(regionSalesFactory.create(regionSalesDTO));
    }

    @Override
    @Transactional
    public RegionSalesEntity update(RegionSalesId id, RegionSalesDTO updateDTO) {

        return regionSalesRepository.save(updateRegionSalesStrategy.update(id,updateDTO));
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
