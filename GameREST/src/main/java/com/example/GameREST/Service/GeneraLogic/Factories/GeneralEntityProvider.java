package com.example.GameREST.Service.GeneraLogic.Factories;

public interface GeneralEntityProvider<T>
{
    <V extends  EntityFactory<T>> V getEntityFactory(Class<T>  entityFactory);
}
