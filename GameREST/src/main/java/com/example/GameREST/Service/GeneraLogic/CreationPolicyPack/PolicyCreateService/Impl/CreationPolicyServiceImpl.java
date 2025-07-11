package com.example.GameREST.Service.GeneraLogic.CreationPolicyPack.PolicyCreateService.Impl;

import com.example.GameREST.Service.GeneraLogic.CreateData.CreateData;
import com.example.GameREST.Service.GeneraLogic.CreateData.CreationPolicyState;
import com.example.GameREST.Service.GeneraLogic.CreationPolicyPack.CreationPolicy;
import com.example.GameREST.Service.GeneraLogic.CreationPolicyPack.PolicyCreateService.CreationPolicyService;
import com.example.GameREST.Service.GeneraLogic.Factories.GeneralCreationPolicyFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

@Component
public class CreationPolicyServiceImpl<T> implements CreationPolicyService<T>
{
    private final GeneralCreationPolicyFactory<T> generalCreationPolicyFactory;
    private final Map<CreationPolicyState, Function<CreateData<T>,T>> createMethodsMap;

    public CreationPolicyServiceImpl(GeneralCreationPolicyFactory<T> generalCreationPolicyFactory) {
        this.generalCreationPolicyFactory = generalCreationPolicyFactory;

        this.createMethodsMap  = Map.of(CreationPolicyState.LINKED, this::createLinked,
                                        CreationPolicyState.STRICT,this::createStrict);
    }

    @Override
    public T create(CreateData<T> createData, CreationPolicyState state)
    {
        return createMethodsMap.get(state).apply(createData);
    }


    public  T createLinked(CreateData<T> createData)
    {
        CreationPolicy<T> creationPolicy = generalCreationPolicyFactory.getLinkedCreationPolicy(createData.entityClass());
        return creationPolicy.apply(createData);

    }


    public T createStrict(CreateData<T> createData) {
        CreationPolicy<T> creationPolicy = generalCreationPolicyFactory.getStrictCreationPolicy(createData.entityClass());
        return creationPolicy.apply(createData);
    }
}
