package com.events.api.application.services;

import com.events.api.domain.coupon.Coupon;
import com.events.api.domain.coupon.CouponRequestDTO;
import com.events.api.domain.event.Event;
import com.events.api.adapters.outbound.repositories.CouponRepository;
import com.events.api.adapters.outbound.repositories.JpaEventRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    private final JpaEventRepository jpaEventRepository;

    public CouponService(CouponRepository couponRepository, JpaEventRepository jpaEventRepository) {
        this.couponRepository = couponRepository;
        this.jpaEventRepository = jpaEventRepository;
    }

    public Coupon addCouponToEvent(UUID eventId, CouponRequestDTO couponRequestDTO) {
        Event event = jpaEventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        Coupon coupon = new Coupon();

        coupon.setCode(couponRequestDTO.code());
        coupon.setDiscount(couponRequestDTO.discount());
        coupon.setValid(new Date(couponRequestDTO.valid()));
        coupon.setEvent(event);

        return couponRepository.save(coupon);
    }

    public List<Coupon> consultCoupons(UUID eventId, Date currentDate) {
        return couponRepository.findByEventIdAndValidAfter(eventId, currentDate);
    }
}
