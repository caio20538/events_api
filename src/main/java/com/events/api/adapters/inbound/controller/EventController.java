package com.events.api.adapters.inbound.controller;

import com.events.api.domain.event.Event;
import com.events.api.domain.event.EventDetailsDTO;
import com.events.api.domain.event.EventRequestDTO;
import com.events.api.domain.event.EventResponseDTO;
import com.events.api.application.services.EventServiceImpl;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/event")
public class EventController {

    private final EventServiceImpl eventServiceImpl;

    public EventController(EventServiceImpl eventServiceImpl) {
        this.eventServiceImpl = eventServiceImpl;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Event> create(@Valid @ModelAttribute EventRequestDTO eventRequestDTO){
        Event newEvent = this.eventServiceImpl.createEvent(eventRequestDTO);
        return ResponseEntity.ok(newEvent);
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getEvents(@RequestParam(defaultValue = "0") int paginaAtual,
                                                            @RequestParam(defaultValue = "0") int size){
        List<EventResponseDTO> allEvents = this.eventServiceImpl.getUpcomingEvents(paginaAtual, size);
        return ResponseEntity.ok(allEvents);

    }

    @GetMapping("/filter")
    public ResponseEntity<List<EventResponseDTO>> filterEvents(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size,
                                                               @RequestParam String city,
                                                               @RequestParam String uf,
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate){
        List<EventResponseDTO> events = eventServiceImpl.getFilteredEvents(page, size, city, uf, startDate, endDate);
        return ResponseEntity.ok(events);

    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDetailsDTO> getEventDetails(@PathVariable UUID eventId) {
        EventDetailsDTO eventDetails = eventServiceImpl.getEventDetails(eventId);
        return ResponseEntity.ok(eventDetails);
    }

    @GetMapping("/search")
    public ResponseEntity<List<EventResponseDTO>> getSearchEvents(@RequestParam String title) {
        List<EventResponseDTO> events = eventServiceImpl.searchEvents(title);
        return ResponseEntity.ok(events);
    }
}
