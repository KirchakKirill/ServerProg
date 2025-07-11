package com.example.GameREST.Service.GamePlatformService.Factories;

import com.example.GameREST.DTO.RequestGamePlatformDTO;
import com.example.GameREST.Entity.GamePlatformEntity;

public interface GamePlatformFactory{
   GamePlatformEntity createFromDto(RequestGamePlatformDTO requestGamePlatformDTO);
}