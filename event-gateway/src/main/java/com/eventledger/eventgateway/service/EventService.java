package com.eventledger.eventgateway.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.eventledger.eventgateway.model.Event;
import com.eventledger.eventgateway.repository.EventRepository;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final WebClient webClient;

    public EventService(EventRepository eventRepository, WebClient accountServiceClient) {
        this.eventRepository = eventRepository;
        this.webClient = accountServiceClient;
    }
    
    public Event processEvent(Event event) {
        if (eventRepository.existsById(event.getEventId())) {
            return eventRepository.findById(event.getEventId()).get();
        }

        Event saved = eventRepository.save(event);

        // Forward to Account Service with trace ID
        webClient.post()
                .uri("/accounts/" + event.getAccountId() + "/transactions")
                .header("X-Trace-Id", UUID.randomUUID().toString()) // propagate trace
                .bodyValue(event)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        return saved;
    }

}
