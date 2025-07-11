package com.example.GameREST.Service.GameService.Factories;

import com.example.GameREST.Service.GameService.CreationPolicy.GameCreationPolicy;

public interface GameCreationPolicyFactory
{
    GameCreationPolicy createStrictGameCreationPolicy();
    GameCreationPolicy createLinkedGameCreationPolicy();
}
