package com.example.GameREST.Service.GamePlatformService.Strategies.Impl;

import com.example.GameREST.Entity.PublisherEntity;
import com.example.GameREST.Service.GamePlatformService.Context.UpdateContext;
import com.example.GameREST.Service.PublisherService.PublisherService;

import java.util.Optional;

public class PublisherUpdateStrategy extends AbstractEntityUpdateStrategy<PublisherEntity>
{
    private  final PublisherService publisherService;

    public PublisherUpdateStrategy(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @Override
    boolean needsUpdate(UpdateContext context) {
        return context.needsPublisherUpdate();
    }

    @Override
    Optional<PublisherEntity> findEntity(UpdateContext context) {
        return publisherService.findPublisherByName(context.getDto().getPublisherName());
    }

    @Override
    void setEntity(UpdateContext context, PublisherEntity entity) {
        context.setPublisher(entity);
    }
}
