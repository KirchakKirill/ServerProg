package com.example.GameREST.Service.GenreService.Impl;

import com.example.GameREST.Entity.GenreEntity;
import com.example.GameREST.Repository.GenreRepository;
import com.example.GameREST.Service.GeneraLogic.CreateData.CreateData;
import com.example.GameREST.Service.GeneraLogic.CreateData.CreationPolicyState;
import com.example.GameREST.Service.GeneraLogic.CreationPolicyPack.PolicyCreateService.CreationPolicyService;
import com.example.GameREST.Service.GeneraLogic.Validators.Context.ValidateContext;
import com.example.GameREST.Service.GeneraLogic.Validators.ValidateService.ValidatorService;
import com.example.GameREST.Service.GenreService.GenreService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final ValidatorService<GenreEntity> validatorService;
    private final CreationPolicyService<GenreEntity> genreCreationPolicyService;

    public GenreServiceImpl(GenreRepository genreRepository,
                            ValidatorService<GenreEntity> validatorService,
                            CreationPolicyService<GenreEntity> genreCreationPolicyService) {
        this.genreRepository = genreRepository;
        this.validatorService = validatorService;
        this.genreCreationPolicyService = genreCreationPolicyService;
    }

    @Override
    public Optional<GenreEntity> findGenreByName(String genre) {
        return genreRepository.findEntityByName(genre);
    }

    @Override
    @Transactional
    public GenreEntity create(String genreName, CreationPolicyState creationPolicyState) {
        CreateData<GenreEntity> data = new CreateData<>(genreName,GenreEntity.class);
        return  genreRepository.save(genreCreationPolicyService.create(data,creationPolicyState));
    }

    @Override
    @Transactional
    public void update(Long id, String genreName, boolean forceUpdate) {
        GenreEntity genre = genreRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Жанр не найден"));
        ValidateContext<GenreEntity> validateContext = new ValidateContext<>(genre,forceUpdate,genreName,GenreEntity.class);
        validatorService.validateUpdate(validateContext);
        genreRepository.update(id,genreName);
    }

    @Override
    public Page<GenreEntity> findAllWithPaging(Pageable pageable) {
        return genreRepository.findAll(pageable);
    }

    @Override
    public Optional<GenreEntity> findById(Long id) {
        return genreRepository.findById(id);
    }

    @Override
    @Transactional
    public void delete(Long id,boolean forceDelete) {
        GenreEntity genre = genreRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Жанр не найден"));

        ValidateContext<GenreEntity> validateContext = new ValidateContext<>(genre,forceDelete,null,GenreEntity.class);
        validatorService.validateDelete(validateContext);
        genreRepository.delete(genre);

    }
}


