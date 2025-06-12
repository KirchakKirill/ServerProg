package com.example.GameREST.Service.Implementations;


import com.example.GameREST.Entity.PublisherEntity;
import com.example.GameREST.Repository.PublisherRepository;
import com.example.GameREST.Service.Interfaces.PublisherService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PublisherServiceImplementation implements PublisherService {

    @Autowired
    private PublisherRepository publisherRepository;

    @Override
    public Optional<PublisherEntity> findPublisherById(Long id) {
        return publisherRepository.findPublisherById(id);
    }

    @Override
    @Transactional
    public PublisherEntity save(String publisherName) {
        PublisherEntity publisher = returnIfExists(publisherName);

        if (publisher == null) {
            PublisherEntity publisherEntity = PublisherEntity.builder()
                    .publisherName(publisherName)
                    .build();
            return publisherRepository.save(publisherEntity);

        }
        else{
            return  publisher;
        }




    }

    @Override
    public Optional<PublisherEntity> findPublisherByName(String publisherName) {
        return  publisherRepository.findPublisherByName(publisherName);
    }

    @Override
    public void update(Long id, String publisherName) {
        publisherRepository.update(id,publisherName);
    }

    public PublisherEntity returnIfExists(String publisherName){
        Optional<PublisherEntity> publisherEntity =  publisherRepository.findPublisherByName(publisherName);
        PublisherEntity currentPublisher;

        if (publisherEntity.isPresent())
        {
            currentPublisher = publisherEntity.get();
            return  currentPublisher;
        }
        else{
            return null;

        }
    }




}
