package com.example.GameREST.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface GeneralRepository<T,ID> extends JpaRepository<T,ID>
{
    Integer countLinksForEntity(T entity);
    Optional<Long> findIdByName(String name);
    Optional<T> findEntityByName(String name);
}
