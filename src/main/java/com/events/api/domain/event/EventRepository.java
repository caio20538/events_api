package com.events.api.domain.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository {

    Event save(Event event);

    Optional<Event> findById(UUID id);

    List<Event> findAll();

    void deleteById(UUID id);

    public Page<Event> findUpcomingEvents(int page, int size);

    Page<Event> findFilteredEvents(String city,
                                   String uf,
                                   Date startDate,
                                   Date endDate,
                                   int page,
                                   int size);


    List<Event> findEventsByTitle(String title);
}
