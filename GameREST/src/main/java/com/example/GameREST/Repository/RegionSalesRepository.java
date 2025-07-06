package com.example.GameREST.Repository;

import com.example.GameREST.DTO.RegionSalesId;
import com.example.GameREST.Entity.RegionEntity;
import com.example.GameREST.Entity.RegionSalesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface RegionSalesRepository extends JpaRepository<RegionSalesEntity, RegionSalesId>
{

}
