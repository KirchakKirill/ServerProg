package com.example.GameREST.Service.RegionService.Impl;

import com.example.GameREST.Entity.RegionEntity;
import com.example.GameREST.Repository.RegionRepository;
import com.example.GameREST.Service.GeneraLogic.CreateData.CreateData;
import com.example.GameREST.Service.GeneraLogic.CreateData.CreationPolicyState;
import com.example.GameREST.Service.GeneraLogic.CreationPolicyPack.PolicyCreateService.CreationPolicyService;
import com.example.GameREST.Service.GeneraLogic.Validators.Context.ValidateContext;
import com.example.GameREST.Service.GeneraLogic.Validators.ValidateService.ValidatorService;
import com.example.GameREST.Service.RegionService.RegionService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;
    private final ValidatorService<RegionEntity> validatorService;
    private final CreationPolicyService<RegionEntity> creationPolicyService;

    public RegionServiceImpl(RegionRepository regionRepository, ValidatorService<RegionEntity> validatorService, CreationPolicyService<RegionEntity> creationPolicyService) {
        this.regionRepository = regionRepository;
        this.validatorService = validatorService;
        this.creationPolicyService = creationPolicyService;
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
    public RegionEntity create(String regionName, CreationPolicyState creationPolicyState) {
        CreateData<RegionEntity> createData = new CreateData<>(regionName,RegionEntity.class);
        return regionRepository.save(creationPolicyService.create(createData,creationPolicyState));
    }

    @Override
    @Transactional
    public void update(Long id, String regionName,boolean forceUpdate) {

        RegionEntity region = regionRepository.findById(id)
                .orElseThrow(()->new NoSuchElementException("Регион не найден"));

        ValidateContext<RegionEntity> validateContext = new ValidateContext<>(region,forceUpdate,regionName,RegionEntity.class);
        validatorService.validateUpdate(validateContext);
        region.setRegionName(regionName);
        regionRepository.save(region);
    }

    @Override
    @Transactional
    public void delete(Long id, boolean forceDelete)
    {

        RegionEntity region = regionRepository.findById(id)
                .orElseThrow(()->new NoSuchElementException("Регион не найден"));

        ValidateContext<RegionEntity> validateContext  = new ValidateContext<>(region,forceDelete,null,RegionEntity.class);
        validatorService.validateDelete(validateContext);
        regionRepository.delete(region);

    }

    @Override
    public Optional<RegionEntity> findByRegionName(String regionName) {
        return regionRepository.findEntityByName(regionName);
    }
}
