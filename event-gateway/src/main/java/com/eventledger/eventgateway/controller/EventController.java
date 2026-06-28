package com.eventledger.eventgateway.controller;

import com.eventledger.eventgateway.model.Event;
import com.eventledger.eventgateway.repository.EventRepository;
import com.eventledger.eventgateway.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;
    private final EventRepository eventRepository;

    public EventController(EventService eventService, EventRepository eventRepository) {
        this.eventService = eventService;
        this.eventRepository = eventRepository;
    }

    @PostMapping("/events")
    public ResponseEntity<Event> createEvent(@RequestBody Event event,
                                             @RequestHeader(value = "X-Trace-Id", required = false) String traceId) {
        Event saved = eventService.processEvent(event, traceId);
        return ResponseEntity.ok(saved);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable String id) {
        return eventRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Event>> getEventsByAccount(@RequestParam String account) {
        return ResponseEntity.ok(eventRepository.findByAccountIdOrderByEventTimestampAsc(account));
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Event Gateway is UP");
    }
}
