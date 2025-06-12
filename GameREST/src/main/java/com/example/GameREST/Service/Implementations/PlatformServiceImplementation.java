package com.example.GameREST.Service.Implementations;

import com.example.GameREST.Entity.PlatformEntity;
import com.example.GameREST.Repository.PlatformRepository;
import com.example.GameREST.Service.Interfaces.PlatformService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlatformServiceImplementation implements PlatformService
{
    @Autowired
    private PlatformRepository platformRepository;


    @Override
    public Optional<PlatformEntity> findPlatformByName(String name) {
        return platformRepository.findPlatformByName(name);
    }

    @Override
    @Transactional
    public PlatformEntity save(String platformName) {
        PlatformEntity platform = returnIfExists(platformName);
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
    public void update(Long id, String platformName) {
        platformRepository.update(id,platformName);
    }

    public PlatformEntity returnIfExists(String platformName){
        Optional<PlatformEntity> platformEntity =  platformRepository.findPlatformByName(platformName);
        PlatformEntity currentPlatform;

        if (platformEntity.isPresent())
        {
            currentPlatform = platformEntity.get();
            return currentPlatform;
        }
        else{
            return null;

        }
    }
}
