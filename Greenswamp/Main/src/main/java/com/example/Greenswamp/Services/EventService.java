package com.example.Greenswamp.Services;

import com.example.Greenswamp.Entity.Event;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventService {
    public List<Event> getAllEventsWithContent();
}
