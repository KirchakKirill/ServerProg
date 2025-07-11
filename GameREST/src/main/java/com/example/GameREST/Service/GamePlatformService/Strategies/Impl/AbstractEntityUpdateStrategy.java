package com.example.GameREST.Service.GamePlatformService.Strategies.Impl;

import com.example.GameREST.Service.GamePlatformService.Context.UpdateContext;
import com.example.GameREST.Service.GamePlatformService.Strategies.UpdateStrategy;

import java.util.NoSuchElementException;
import java.util.Optional;

public abstract class AbstractEntityUpdateStrategy<T> implements UpdateStrategy
{
    abstract boolean needsUpdate(UpdateContext context);
    abstract Optional<T> findEntity(UpdateContext context);
    abstract void setEntity(UpdateContext context, T entity);

    @Override
    public void update(UpdateContext context) {

        if (needsUpdate(context))
        {
            T entity = findEntity(context)
                    .orElseThrow(()->new NoSuchElementException("Ресурс не найден"));
            setEntity(context,entity);

        }

    }
}
