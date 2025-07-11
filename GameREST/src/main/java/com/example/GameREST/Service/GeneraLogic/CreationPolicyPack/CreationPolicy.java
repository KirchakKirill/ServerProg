package com.example.GameREST.Service.GeneraLogic.CreationPolicyPack;

import com.example.GameREST.Service.GeneraLogic.CreateData.CreateData;

public interface CreationPolicy<T>
{
    T apply(CreateData<T> createData);
}
