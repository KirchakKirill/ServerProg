package com.example.Greenswamp.Services;


import com.example.Greenswamp.Entity.Event;
import com.example.Greenswamp.Repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImp implements EventService {

    private final EventRepository eventRepository;

    public EventServiceImp(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }


    @Override
    public List<Event> getAllEventsWithContent() {
        return eventRepository.getAllEvents();
    }
}
