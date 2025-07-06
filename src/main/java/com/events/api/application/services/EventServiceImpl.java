package com.events.api.application.services;

import com.events.api.adapters.outbound.storage.ImageUploadAdapter;
import com.events.api.application.usecases.EventUserCases;
import com.events.api.domain.address.Address;
import com.events.api.domain.coupon.Coupon;
import com.events.api.domain.event.*;
import com.events.api.utils.mappers.EventMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EventServiceImpl implements EventUserCases {

    private final EventRepository repository;
    private final AddressService addressService;
    private final CouponService couponService;
    private final EventMapper mapper;
    private final ImageUploadAdapter imageUploadAdapter;

    public EventServiceImpl(EventRepository repository, AddressService addressService, CouponService couponService, EventMapper mapper, ImageUploadAdapter imageUploadAdapter) {
        this.repository = repository;
        this.addressService = addressService;
        this.couponService = couponService;
        this.mapper = mapper;
        this.imageUploadAdapter = imageUploadAdapter;
    }

    public Event createEvent(EventRequestDTO data){
        String imgUrl = null;

        if (data.image() != null)
            imgUrl = imageUploadAdapter.uploadImage(data.image());

        Event newEvent = mapper.dtoToEntity(data, imgUrl);

        repository.save(newEvent);

        if (!data.remote())
            this.addressService.createAddress(data,newEvent);

        return newEvent;
    }

    public List<EventResponseDTO> getUpcomingEvents(int page, int size){
        Page<Event> eventPage = this.repository.findUpcomingEvents(page, size);
        return getEventResponseDTOS(eventPage);
    }

    private static List<EventResponseDTO> getEventResponseDTOS(@org.jetbrains.annotations.NotNull Page<Event> eventPage) {
        return eventPage.map(event -> new EventResponseDTO(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getDate(),
                        event.getAddress() != null ? event.getAddress().getCity() : "",
                        event.getAddress() != null ? event.getAddress().getUf() : "",
                        event.isRemote(),
                        event.getEventUrl(),
                        event.getImgUrl())
                )
                .stream().toList();
    }

    public List<EventResponseDTO> getFilteredEvents(int page, int size, String city, String uf, Date startDate, Date endDate) {
        city = (city != null) ? city : "";
        uf = (uf != null) ? uf : "";
        startDate = (startDate != null) ? startDate : new Date(0);
        endDate = (endDate != null) ? endDate : new Date();

        Page<Event> eventPage = this.repository.findFilteredEvents(city, uf, startDate, endDate, page, size);
        return getEventResponseDTOS(eventPage);
    }

    public List<EventResponseDTO> searchEvents(String title){
        title = (title != null) ? title : "";

        List<Event> eventsList = this.repository.findEventsByTitle(title);
        return eventsList.stream().map(event -> new EventResponseDTO(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getDate(),
                        event.getAddress() != null ? event.getAddress().getCity() : "",
                        event.getAddress() != null ? event.getAddress().getUf() : "",
                        event.isRemote(),
                        event.getEventUrl(),
                        event.getImgUrl())
                )
                .toList();
    }

    public EventDetailsDTO getEventDetails(UUID eventId) {
        Event event = repository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        Optional<Address> address = addressService.findByEventId(eventId);

        List<Coupon> coupons = couponService.consultCoupons(eventId, new Date());

        return mapper.domainToDetailsDTO(event, address, coupons);
    }
}
