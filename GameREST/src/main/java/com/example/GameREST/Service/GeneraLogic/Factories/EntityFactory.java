package com.example.GameREST.Service.GeneraLogic.Factories;

import com.example.GameREST.Service.GeneraLogic.CreateData.CreateData;

public interface EntityFactory<T>
{
    T build(CreateData<T> createData);
    Class<T> getEntityType();
}
