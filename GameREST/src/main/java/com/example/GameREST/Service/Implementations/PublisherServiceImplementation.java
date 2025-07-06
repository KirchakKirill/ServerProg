package com.example.GameREST.Service.Implementations;


import com.example.GameREST.Entity.PlatformEntity;
import com.example.GameREST.Entity.PublisherEntity;
import com.example.GameREST.Exception.BusinessLogicException;
import com.example.GameREST.Exception.UniqueConstraintViolationException;
import com.example.GameREST.Repository.PublisherRepository;
import com.example.GameREST.Service.Interfaces.GamePublisherService;
import com.example.GameREST.Service.Interfaces.PublisherService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PublisherServiceImplementation implements PublisherService {


    private final PublisherRepository publisherRepository;



    public PublisherServiceImplementation(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;


    }

    @Override
    public Optional<PublisherEntity> findPublisherById(Long id) {
        return publisherRepository.findPublisherById(id);
    }

    @Override
    public PublisherEntity createOrFind(String publisherName) {
        PublisherEntity publisher = publisherRepository
                .findPublisherByName(publisherName).orElse(null);

        if (publisher == null) {

            publisher = PublisherEntity.builder()
                    .publisherName(publisherName)
                    .build();
            return publisherRepository.save(publisher);

        } else {

            return publisher;
        }
    }

    @Override
    @Transactional
    public PublisherEntity create(String publisherName) {

        PublisherEntity existing = publisherRepository.findPublisherByName(publisherName).orElse(null);
        if (existing != null) {
            throw new UniqueConstraintViolationException(
                    "Вы пытаетесь добавить платформу, игнорируя ограничение уникальности названия",
                    PublisherEntity.class.getSimpleName(),
                    existing.getId(),
                    publisherName);
        }

        return publisherRepository.save(PublisherEntity
                .builder()
                .publisherName(publisherName)
                .build());
    }

    @Override
    @Transactional
    public void delete(Long id, boolean forceDelete) {
        PublisherEntity publisher = publisherRepository.findPublisherById(id)
                        .orElseThrow(()->new NoSuchElementException("Издатель не найден"));


        Integer count = publisherRepository.existsByPublisher(publisher);

        if (count > 0 && !forceDelete) {
            throw new BusinessLogicException("Издатель имеет " + count + " связей типа [Игра - Издатель]." +
                    "Убедитесь, что удаление данной сущности не повлияет на работу вашего приложения." +
                    "Если уверенны, то forceDelete = true");
        }

        publisherRepository.delete(publisher);
    }


    @Override
    public Optional<PublisherEntity> findPublisherByName(String publisherName) {
        return  publisherRepository.findPublisherByName(publisherName);
    }

    @Override
    @Transactional
    public void update(Long id, String publisherName, boolean forceUpdate)
    {
        PublisherEntity publisher = publisherRepository.findPublisherById(id)
                .orElseThrow(()-> new NoSuchElementException("Издатель не найден"));

        Integer count = publisherRepository.existsByPublisher(publisher);

        if (count > 0 && !forceUpdate) {
            throw new BusinessLogicException("Издатель имеет " + count + " связей типа [Игра - Издатель]." +
                    "Убедитесь, что изменение данной сущности не повлияет на работу вашего приложения." +
                    "Если уверенны, то forceUpdate = true");
        }

        PublisherEntity existing = publisherRepository.findPublisherByName(publisherName).orElse(null);

        if (existing == null)
        {
            publisherRepository.update(id,publisherName);
        }
        else {
            throw new UniqueConstraintViolationException("Издатель  с таким названием уже существует",
                    PublisherEntity.class.getSimpleName(),
                    existing.getId(),
                    publisherName);
        }


    }

    @Override
    public Page<PublisherEntity> findAllWithPage(Pageable pageable) {
        return publisherRepository.findAll(pageable);
    }


}
