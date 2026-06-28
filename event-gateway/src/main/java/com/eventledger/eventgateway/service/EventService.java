package com.eventledger.eventgateway.service;

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
        // Idempotency check
        if (eventRepository.existsById(event.getEventId())) {
            return eventRepository.findById(event.getEventId()).get();
        }

        // Save locally
        Event saved = eventRepository.save(event);

        // Forward to Account Service
        webClient.post()
                .uri("/accounts/" + event.getAccountId() + "/transactions")
                .bodyValue(event)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        return saved;
    }
}
