package com.example.GameREST.Service.GenreService;

import com.example.GameREST.Entity.GenreEntity;
import com.example.GameREST.Service.GeneraLogic.CreateData.CreationPolicyState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface GenreService {

    Optional<GenreEntity> findGenreByName(String genre);
    GenreEntity create(String genreName, CreationPolicyState creationPolicyState);
    void update(Long id,String genreName,boolean forceUpdate);
    Page<GenreEntity> findAllWithPaging(Pageable pageable);
    Optional<GenreEntity> findById(Long id);
    void delete(Long id,boolean forceDelete);

}
