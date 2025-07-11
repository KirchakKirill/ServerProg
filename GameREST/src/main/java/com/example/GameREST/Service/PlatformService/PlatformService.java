package com.example.GameREST.Service.PlatformService;


import com.example.GameREST.Entity.PlatformEntity;
import com.example.GameREST.Service.GeneraLogic.CreateData.CreationPolicyState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface PlatformService {

    Optional<PlatformEntity> findPlatformByName(String name);

    void update(Long id,String platformName,boolean forceUpdate);

    PlatformEntity create(String platformName, CreationPolicyState creationPolicyState);

    void delete(Long id,boolean forceDelete);

    Page<PlatformEntity> findAllWithPaging(Pageable pageable);

    Optional<PlatformEntity> findById(Long id);
}
