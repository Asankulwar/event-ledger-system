package com.eventledger.eventgateway.controller;

import com.eventledger.eventgateway.model.Event;
import com.eventledger.eventgateway.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event,
                                             @RequestHeader(value = "X-Trace-Id", required = false) String traceId) {
        // Pass both Event and traceId to the service
        Event saved = eventService.processEvent(event, traceId);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable String id,
                                          @RequestHeader(value = "X-Trace-Id", required = false) String traceId) {
        // Optional: you can reuse traceId here for logging
        return eventService.getEventById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
