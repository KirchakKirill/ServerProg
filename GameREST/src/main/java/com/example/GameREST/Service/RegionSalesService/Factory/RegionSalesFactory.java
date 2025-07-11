package com.example.GameREST.Service.RegionSalesService.Factory;

import com.example.GameREST.DTO.RegionSalesDTO;
import com.example.GameREST.Entity.RegionSalesEntity;

public interface RegionSalesFactory
{
    RegionSalesEntity create(RegionSalesDTO regionSalesDTO);
}
