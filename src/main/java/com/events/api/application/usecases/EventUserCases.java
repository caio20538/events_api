package com.events.api.application.usecases;

import com.events.api.domain.event.Event;
import com.events.api.domain.event.EventDetailsDTO;
import com.events.api.domain.event.EventRequestDTO;
import com.events.api.domain.event.EventResponseDTO;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface EventUserCases{

    public Event createEvent(EventRequestDTO data);

    public List<EventResponseDTO> getUpcomingEvents(int page, int size);

    public List<EventResponseDTO> getFilteredEvents(int page, int size, String city, String uf, Date startDate, Date endDate);

    public List<EventResponseDTO> searchEvents(String title);

    public EventDetailsDTO getEventDetails(UUID eventId);
}
