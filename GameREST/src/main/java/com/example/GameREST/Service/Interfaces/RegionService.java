package com.example.GameREST.Service.Interfaces;

import com.example.GameREST.DTO.RegionSalesDTO;
import com.example.GameREST.DTO.RegionSalesId;
import com.example.GameREST.Entity.RegionEntity;
import com.example.GameREST.Entity.RegionSalesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface RegionService {


    Page<RegionEntity> findAllWithPaging(Pageable pageable);
    Optional<RegionEntity> findById(Long id);
    RegionEntity create(String regionName);
    void update (Long id,String regionName,boolean forceUpdate);
    void delete(Long id, boolean forceDelete);
    Optional<RegionEntity> findByRegionName(String regionName);
}
