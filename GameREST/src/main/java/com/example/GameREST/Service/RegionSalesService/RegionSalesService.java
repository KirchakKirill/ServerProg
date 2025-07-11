package com.example.GameREST.Service.RegionSalesService;

import com.example.GameREST.DTO.RegionSalesDTO;
import com.example.GameREST.DTO.RegionSalesId;
import com.example.GameREST.Entity.RegionSalesEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface RegionSalesService {

    RegionSalesEntity create(RegionSalesDTO regionSalesDTO);

    RegionSalesEntity update(RegionSalesId id, RegionSalesDTO regionSalesDTO);

    void delete(RegionSalesId id);

    Page<RegionSalesEntity> findAll(Pageable pageable);

    Optional<RegionSalesEntity> findById(RegionSalesId id);

}
