package com.example.GameREST.Service.Implementations;

import com.example.GameREST.Entity.GenreEntity;
import com.example.GameREST.Entity.PlatformEntity;
import com.example.GameREST.Exception.BusinessLogicException;
import com.example.GameREST.Exception.UniqueConstraintViolationException;
import com.example.GameREST.Repository.PlatformRepository;
import com.example.GameREST.Service.Interfaces.GamePlatformSevice;
import com.example.GameREST.Service.Interfaces.PlatformService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PlatformServiceImplementation implements PlatformService
{

    private final PlatformRepository platformRepository;


    public PlatformServiceImplementation(PlatformRepository platformRepository) {
        this.platformRepository = platformRepository;
    }

    @Override
    public Optional<PlatformEntity> findPlatformByName(String name) {
        return platformRepository.findPlatformByName(name);
    }

    @Override
    public PlatformEntity createOrFind(String platformName) {
        PlatformEntity platform = platformRepository.findPlatformByName(platformName).orElse(null);
        if(platform == null)
        {
             platform = PlatformEntity.builder()
                     .platformName(platformName)
                     .build();
             return platformRepository.save(platform);
        }
        else {
            return platform;
        }
    }

    @Override
    @Transactional
    public PlatformEntity create(String platformName)
    {
        PlatformEntity existing = platformRepository.findPlatformByName(platformName).orElse(null);
        if (existing != null) {
            throw new UniqueConstraintViolationException(
                    "Вы пытаетесь добавить платформу, игнорируя ограничение уникальности названия",
                    PlatformEntity.class.getSimpleName(),
                    existing.getId(),
                    platformName);
        }
        return platformRepository.save(PlatformEntity.builder()
                .platformName(platformName)
                .build());
    }

    @Override
    @Transactional
    public void delete(Long id, boolean forceDelete) {
        PlatformEntity platform = platformRepository.findById(id)
                        .orElseThrow(()->new NoSuchElementException("Платформа не найдена"));
        Integer count = platformRepository.existsGamePlatformByPlatform(platform);

        if (count > 0 && !forceDelete)
        {
            throw new BusinessLogicException("Платформа имеет " + count + " связей типа [Игра - Платформа]." +
                    "Убедитесь, что удаление данной сущности не повлияет на работу вашего приложения." +
                    "Если уверенны, то forceDelete = true");
        }
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

        Integer count = platformRepository.existsGamePlatformByPlatform(platform);

        if (count > 0 && !forceUpdate) {
            throw new BusinessLogicException("Платформа имеет " + count + " связей типа [Игра - Платформа]." +
                    "Убедитесь, что изменение данной сущности не повлияет на работу вашего приложения." +
                    "Если уверенны, то forceUpdate = true");
        }
        PlatformEntity existing = platformRepository.findPlatformByName(platformName).orElse(null);
        if (existing==null)
        {
            platformRepository.update(id,platformName);
        }
        else {
            throw new UniqueConstraintViolationException("Платформа с таким названием уже существует",
                    PlatformEntity.class.getSimpleName(),
                    existing.getId(),
                    platformName);
        }


    }

}
