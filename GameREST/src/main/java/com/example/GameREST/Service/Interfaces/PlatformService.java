package com.example.GameREST.Service.Interfaces;


import com.example.GameREST.Entity.PlatformEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface PlatformService {

    Optional<PlatformEntity> findPlatformByName(String name);

    PlatformEntity save(String platformName);

     void update(Long id,String platformName);
}
