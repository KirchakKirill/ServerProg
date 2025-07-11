package com.example.GameREST.Service.GeneraLogic.Validators.Context;

public record ValidateContext<T>(T entity, boolean forceAction, String entityUpdateName,Class<T> entityClass) {
}
