package com.example.GameREST.Service.PublisherService.Impl;


import com.example.GameREST.Entity.PublisherEntity;
import com.example.GameREST.Repository.PublisherRepository;
import com.example.GameREST.Service.GeneraLogic.CreateData.CreateData;
import com.example.GameREST.Service.GeneraLogic.CreateData.CreationPolicyState;
import com.example.GameREST.Service.GeneraLogic.CreationPolicyPack.PolicyCreateService.CreationPolicyService;
import com.example.GameREST.Service.GeneraLogic.Validators.Context.ValidateContext;
import com.example.GameREST.Service.GeneraLogic.Validators.ValidateService.ValidatorService;
import com.example.GameREST.Service.PublisherService.PublisherService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;
    private final ValidatorService<PublisherEntity> validatorService;
    private final CreationPolicyService<PublisherEntity> creationPolicyService;

    public PublisherServiceImpl(PublisherRepository publisherRepository, ValidatorService<PublisherEntity> validatorService, CreationPolicyService<PublisherEntity> creationPolicyService) {
        this.publisherRepository = publisherRepository;
        this.validatorService = validatorService;
        this.creationPolicyService = creationPolicyService;
    }

    @Override
    public Optional<PublisherEntity> findPublisherById(Long id) {
        return publisherRepository.findPublisherById(id);
    }

    @Override
    @Transactional
    public PublisherEntity create(String publisherName, CreationPolicyState creationPolicyState) {

        CreateData<PublisherEntity> createData = new CreateData<>(publisherName,PublisherEntity.class);
        return publisherRepository.save(creationPolicyService.create(createData,creationPolicyState));
    }

    @Override
    @Transactional
    public void delete(Long id, boolean forceDelete) {
        PublisherEntity publisher = publisherRepository.findPublisherById(id)
                        .orElseThrow(()->new NoSuchElementException("Издатель не найден"));
        ValidateContext<PublisherEntity> validateContext = new ValidateContext<>(publisher,forceDelete,null,PublisherEntity.class);
        validatorService.validateDelete(validateContext);
        publisherRepository.delete(publisher);
    }


    @Override
    public Optional<PublisherEntity> findPublisherByName(String publisherName) {
        return  publisherRepository.findEntityByName(publisherName);
    }

    @Override
    @Transactional
    public void update(Long id, String publisherName, boolean forceUpdate)
    {
        PublisherEntity publisher = publisherRepository.findPublisherById(id)
                .orElseThrow(()-> new NoSuchElementException("Издатель не найден"));
        ValidateContext<PublisherEntity> validateContext = new ValidateContext<>(publisher,forceUpdate,publisherName,PublisherEntity.class);
        validatorService.validateUpdate(validateContext);
        publisherRepository.update(id,publisherName);



    }

    @Override
    public Page<PublisherEntity> findAllWithPage(Pageable pageable) {
        return publisherRepository.findAll(pageable);
    }


}
