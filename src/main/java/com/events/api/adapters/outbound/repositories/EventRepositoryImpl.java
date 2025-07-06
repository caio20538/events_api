package com.events.api.adapters.outbound.repositories;

import com.events.api.adapters.outbound.entities.JpaEventEntity;
import com.events.api.domain.event.Event;
import com.events.api.domain.event.EventRepository;
import com.events.api.utils.mappers.EventMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class EventRepositoryImpl implements EventRepository {

    private final JpaEventRepository jpaEventRepository;
    private final EventMapper eventMapper;

    public EventRepositoryImpl(JpaEventRepository jpaEventRepository, EventMapper eventMapper) {
        this.jpaEventRepository = jpaEventRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    public Event save(Event event) {
        JpaEventEntity eventEntity = new JpaEventEntity(event);
        this.jpaEventRepository.save(eventEntity);
        return new Event(eventEntity.getId(),
                eventEntity.getTitle(),
                eventEntity.getDescription(),
                eventEntity.getImgUrl(),
                eventEntity.getEventUrl(),
                eventEntity.isRemote(),
                eventEntity.getDate());
    }

    @Override
    public Optional<Event> findById(UUID id) {
        Optional<JpaEventEntity> eventEntity = this.jpaEventRepository.findById(id);
        return eventEntity.map(eventMapper::jpaToDomain);
    }

    @Override
    public List<Event> findAll() {
        return this.jpaEventRepository.findAll().stream()
                .map(entity -> new Event(
                        entity.getId(),
                        entity.getTitle(),
                        entity.getDescription(),
                        entity.getImgUrl(),
                        entity.getEventUrl(),
                        entity.isRemote(),
                        entity.getDate()))
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        this.jpaEventRepository.deleteById(id);
    }

    @Override
    public Page<Event> findUpcomingEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.jpaEventRepository.findUpcomingEvents(new Date(), pageable);
    }

    @Override
    public Page<Event> findFilteredEvents(String city, String uf, Date startDate, Date endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.jpaEventRepository.findFilteredEvents(city, uf, startDate, endDate, pageable);
    }

    @Override
    public List<Event> findEventsByTitle(String title) {
        return this.jpaEventRepository.findEventsByTitle(title);
    }
}
