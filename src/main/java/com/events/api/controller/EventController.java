package com.events.api.controller;

import com.events.api.domain.event.Event;
import com.events.api.domain.event.EventRequestDTO;
import com.events.api.domain.event.EventResponseDTO;
import com.events.api.services.EventService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/event")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Event> create(@Valid @ModelAttribute EventRequestDTO eventRequestDTO){
        Event newEvent = this.eventService.createEvent(eventRequestDTO);
        return ResponseEntity.ok(newEvent);
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getEvents(@RequestParam(defaultValue = "0") int paginaAtual,
                                                            @RequestParam(defaultValue = "0") int size){
        List<EventResponseDTO> allEvents = this.eventService.getUpcomingEvents(paginaAtual, size);
        return ResponseEntity.ok(allEvents);

    }

    @GetMapping("/filter")
    public ResponseEntity<List<EventResponseDTO>> filterEvents(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size,
                                                               @RequestParam String city,
                                                               @RequestParam String uf,
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate){
        List<EventResponseDTO> events = eventService.getFilteredEvents(page, size, city, uf, startDate, endDate);
        return ResponseEntity.ok(events);

    }
}
