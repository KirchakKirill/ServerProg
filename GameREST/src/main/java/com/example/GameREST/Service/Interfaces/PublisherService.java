package com.example.GameREST.Service.Interfaces;

import com.example.GameREST.Entity.PublisherEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface PublisherService {


    Optional<PublisherEntity> findPublisherById(@Param("id") Long id);
    PublisherEntity createOrFind(String publisherName);
    Optional<PublisherEntity> findPublisherByName(@Param("publisherName") String publisherName);
    void update(Long id,String publisherName,boolean forceUpdate);
    Page<PublisherEntity> findAllWithPage(Pageable pageable);
    PublisherEntity create(String publisherName);
    void delete(Long id,boolean forceDelete);
}
