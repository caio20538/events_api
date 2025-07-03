package com.events.api.services;

import com.events.api.domain.coupon.Coupon;
import com.events.api.domain.coupon.CouponRequestDTO;
import com.events.api.domain.event.Event;
import com.events.api.repositories.CouponRepository;
import com.events.api.repositories.EventRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    private final EventRepository eventRepository;

    public CouponService(CouponRepository couponRepository, EventRepository eventRepository) {
        this.couponRepository = couponRepository;
        this.eventRepository = eventRepository;
    }

    public Coupon addCouponToEvent(UUID eventId, CouponRequestDTO couponRequestDTO) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        Coupon coupon = new Coupon();

        coupon.setCode(couponRequestDTO.code());
        coupon.setDiscount(couponRequestDTO.discount());
        coupon.setValid(new Date(couponRequestDTO.valid()));
        coupon.setEvent(event);

        return couponRepository.save(coupon);
    }
}
