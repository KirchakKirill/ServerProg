package com.example.GameREST.Service.GamePlatformService;

import com.example.GameREST.DTO.RequestGamePlatformDTO;
import com.example.GameREST.DTO.RequestGamePlatformUpdateDTO;
import com.example.GameREST.Entity.GamePlatformEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface GamePlatformService {

    GamePlatformEntity save(RequestGamePlatformDTO requestGamePlatformDTO);

    Page<GamePlatformEntity> getAll(Pageable pageable);

    Optional<GamePlatformEntity> findById(Long id);

    void update(Long id, RequestGamePlatformUpdateDTO requestGamePlatformDTO);

    void delete(Long id);

}
