package com.eventledger.eventgateway.service;

import com.eventledger.eventgateway.model.Event;
import com.eventledger.eventgateway.repository.EventRepository;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event processEvent(Event event) {
        // Idempotency check
        if (eventRepository.existsById(event.getEventId())) {
            return eventRepository.findById(event.getEventId()).get();
        }
        // Save locally
        return eventRepository.save(event);
        // Forward to Account Service will be added later (Commit 5+)
    }
}
