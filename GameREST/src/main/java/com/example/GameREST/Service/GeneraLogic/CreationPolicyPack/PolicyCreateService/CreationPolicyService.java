package com.example.GameREST.Service.GeneraLogic.CreationPolicyPack.PolicyCreateService;

import com.example.GameREST.Service.GeneraLogic.CreateData.CreateData;
import com.example.GameREST.Service.GeneraLogic.CreateData.CreationPolicyState;

public interface CreationPolicyService<T>
{
    T create(CreateData<T> createData, CreationPolicyState state);
}
