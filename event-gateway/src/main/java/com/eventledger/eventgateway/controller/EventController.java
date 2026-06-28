package com.eventledger.eventgateway.controller;

import com.eventledger.eventgateway.model.Event;
import com.eventledger.eventgateway.service.EventService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
@Tag(name = "Events", description = "Event Gateway APIs")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    @Operation(summary = "Submit a transaction event")
    public ResponseEntity<Event> createEvent(@RequestBody Event event,
                                             @RequestHeader(value = "X-Trace-Id", required = false) String traceId) {
        Event saved = eventService.processEvent(event, traceId);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a single event by ID")
    public ResponseEntity<Event> getEvent(@PathVariable String id,
                                          @RequestHeader(value = "X-Trace-Id", required = false) String traceId) {
        return eventService.getEventById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "List events for an account")
    public ResponseEntity<List<Event>> getEventsByAccount(@RequestParam String accountId,
                                                          @RequestHeader(value = "X-Trace-Id", required = false) String traceId) {
        List<Event> events = eventService.getEventsByAccount(accountId);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}

