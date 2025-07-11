package com.example.GameREST.Service.PlatformService.Impl;

import com.example.GameREST.Entity.PlatformEntity;
import com.example.GameREST.Repository.PlatformRepository;
import com.example.GameREST.Service.GeneraLogic.CreateData.CreateData;
import com.example.GameREST.Service.GeneraLogic.CreateData.CreationPolicyState;
import com.example.GameREST.Service.GeneraLogic.CreationPolicyPack.PolicyCreateService.CreationPolicyService;
import com.example.GameREST.Service.GeneraLogic.Validators.Context.ValidateContext;
import com.example.GameREST.Service.GeneraLogic.Validators.ValidateService.ValidatorService;
import com.example.GameREST.Service.PlatformService.PlatformService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PlatformServiceImpl implements PlatformService
{

    private final PlatformRepository platformRepository;
    private final ValidatorService<PlatformEntity> platformValidatorService;
    private final CreationPolicyService<PlatformEntity> creationPolicyService;
    public PlatformServiceImpl(PlatformRepository platformRepository,
                               ValidatorService<PlatformEntity> platformValidatorService,
                               CreationPolicyService<PlatformEntity> creationPolicyService) {
        this.platformRepository = platformRepository;
        this.platformValidatorService = platformValidatorService;
        this.creationPolicyService = creationPolicyService;
    }

    @Override
    public Optional<PlatformEntity> findPlatformByName(String name) {
        return platformRepository.findEntityByName(name);
    }

    @Override
    @Transactional
    public PlatformEntity create(String platformName, CreationPolicyState creationPolicyState)
    {
        CreateData<PlatformEntity> platformCreateData = new CreateData<>(platformName,PlatformEntity.class);
        return platformRepository.save(creationPolicyService.create(platformCreateData,creationPolicyState));
    }

    @Override
    @Transactional
    public void delete(Long id, boolean forceDelete) {
        PlatformEntity platform = platformRepository.findById(id)
                        .orElseThrow(()->new NoSuchElementException("Платформа не найдена"));

        ValidateContext<PlatformEntity> platformValidateContext = new ValidateContext<>(platform,forceDelete,null,PlatformEntity.class);
        platformValidatorService.validateDelete(platformValidateContext);
        platformRepository.delete(platform);
    }

    @Override
    public Page<PlatformEntity> findAllWithPaging(Pageable pageable) {
        return platformRepository.findAll(pageable);
    }

    @Override
    public Optional<PlatformEntity> findById(Long id) {
        return platformRepository.findById(id);
    }

    @Override
    @Transactional
    public void update(Long id, String platformName,boolean forceUpdate) {

        PlatformEntity platform = platformRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Платформа не найдена"));

        ValidateContext<PlatformEntity> platformValidateContext = new ValidateContext<>(platform,forceUpdate,platformName,PlatformEntity.class);
        platformValidatorService.validateUpdate(platformValidateContext);
        platformRepository.update(id,platformName);

    }
}
