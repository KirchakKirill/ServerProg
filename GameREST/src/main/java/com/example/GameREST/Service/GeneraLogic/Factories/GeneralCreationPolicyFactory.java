package com.example.GameREST.Service.GeneraLogic.Factories;

import com.example.GameREST.Service.GeneraLogic.CreationPolicyPack.CreationPolicy;

public interface GeneralCreationPolicyFactory<T>
{
    CreationPolicy<T> getLinkedCreationPolicy(Class<T> entityClass);
    CreationPolicy<T> getStrictCreationPolicy(Class<T> entityClass);
}
