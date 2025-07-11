package com.example.GameREST.Service.RegionSalesService.Strategy;

import com.example.GameREST.DTO.RegionSalesDTO;
import com.example.GameREST.DTO.RegionSalesId;
import com.example.GameREST.Entity.RegionSalesEntity;

public interface UpdateRegionSalesStrategy
{
    RegionSalesEntity update(RegionSalesId id, RegionSalesDTO updateDTO);
}
