package com.eventledger.eventgateway.service;

import com.eventledger.eventgateway.model.Event;
import com.eventledger.eventgateway.repository.EventRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
public class EventService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepository;
    private final WebClient webClient;
    private final Counter eventCounter;

    public EventService(EventRepository eventRepository, WebClient accountServiceClient, MeterRegistry registry) {
        this.eventRepository = eventRepository;
        this.webClient = accountServiceClient;
        // Register a custom counter metric
        this.eventCounter = Counter.builder("events.processed")
                                   .description("Number of events processed by Event Gateway")
                                   .register(registry);
    }

    public Event processEvent(Event event, String traceId) {
        // Idempotency check
        Optional<Event> existing = eventRepository.findById(event.getEventId());
        if (existing.isPresent()) {
            log.info("traceId={} Event already exists with id={}", traceId, event.getEventId());
            return existing.get();
        }

        // Save locally
        Event saved = eventRepository.save(event);
        eventCounter.increment(); // ✅ increment counter
        log.info("traceId={} Saved event locally id={}", traceId, saved.getEventId());

        // Forward to Account Service with trace ID
        webClient.post()
                .uri("/accounts/" + event.getAccountId() + "/transactions")
                .header("X-Trace-Id", traceId)
                .bodyValue(event)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        log.info("traceId={} Forwarded event to Account Service for accountId={}", traceId, event.getAccountId());

        return saved;
    }
}
