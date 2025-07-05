package com.events.api.application.services;

import com.amazonaws.services.s3.AmazonS3;
import com.events.api.domain.address.Address;
import com.events.api.domain.coupon.Coupon;
import com.events.api.domain.event.Event;
import com.events.api.domain.event.EventDetailsDTO;
import com.events.api.domain.event.EventRequestDTO;
import com.events.api.domain.event.EventResponseDTO;
import com.events.api.adapters.outbound.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final AmazonS3 s3Client;
    private final EventRepository repository;
    private final AddressService addressService;
    private final CouponService couponService;

    public EventService(AmazonS3 s3Client, EventRepository repository, AddressService addressService, CouponService couponService) {
        this.s3Client = s3Client;
        this.repository = repository;
        this.addressService = addressService;
        this.couponService = couponService;
    }

    @Value("${aws.bucket.name}")
    private String bucketName;


    public Event createEvent(EventRequestDTO data){
        String imgUrl = null;

        if (data.image() != null)
            imgUrl = this.uploadImage(data.image());

        Event newEvent = new Event();
        newEvent.setTitle(data.title());
        newEvent.setDescription(data.description());
        newEvent.setRemote(data.remote());
        newEvent.setEventUrl(data.eventUrl());
        newEvent.setDate(new Date(data.date()));
        newEvent.setImgUrl(imgUrl);
        newEvent.setRemote(data.remote());

        repository.save(newEvent);

        if (!data.remote())
            this.addressService.createAddress(data,newEvent);

        return newEvent;
    }

    private String uploadImage(MultipartFile multipartFile){
        String fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        try {
            File file = this.convertMultipartFile(multipartFile);
            s3Client.putObject(bucketName,fileName, file);
            file.delete();
            return s3Client.getUrl(bucketName, fileName).toString();
        }catch (Exception e){
            System.out.println("Error ao subir arquivo");
            return "";
        }
    }

    private File convertMultipartFile(MultipartFile multipartFile) throws IOException {
        File convertFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convertFile);
        fos.write(multipartFile.getBytes());
        fos.close();

        return convertFile;
    }

    public List<EventResponseDTO> getUpcomingEvents(int page, int size){
        Pageable pageable = PageRequest.of(page, size);

        Page<Event> eventPage = this.repository.findUpcomingEvents(new Date(), pageable);
        return getEventResponseDTOS(eventPage);
    }

    private static List<EventResponseDTO> getEventResponseDTOS(Page<Event> eventPage) {
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

        Pageable pageable = PageRequest.of(page, size);

        Page<Event> eventPage = this.repository.findFilteredEvents(city, uf, startDate, endDate, pageable);
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

        List<EventDetailsDTO.CouponDTO> couponDTOs = coupons.stream()
                .map(coupon -> new EventDetailsDTO.CouponDTO(
                        coupon.getCode(),
                        coupon.getDiscount(),
                        coupon.getValid()))
                .collect(Collectors.toList());

        return new EventDetailsDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                address.isPresent() ? address.get().getCity() : "",
                address.isPresent() ? address.get().getUf() : "",
                event.getImgUrl(),
                event.getEventUrl(),
                couponDTOs);
    }
}
