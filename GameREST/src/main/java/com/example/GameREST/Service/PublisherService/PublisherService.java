package com.example.GameREST.Service.PublisherService;

import com.example.GameREST.Entity.PublisherEntity;
import com.example.GameREST.Service.GeneraLogic.CreateData.CreationPolicyState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface PublisherService {


    Optional<PublisherEntity> findPublisherById(Long id);
    Optional<PublisherEntity> findPublisherByName(String publisherName);
    void update(Long id,String publisherName,boolean forceUpdate);
    Page<PublisherEntity> findAllWithPage(Pageable pageable);
    PublisherEntity create(String publisherName, CreationPolicyState creationPolicyState);
    void delete(Long id,boolean forceDelete);
}
