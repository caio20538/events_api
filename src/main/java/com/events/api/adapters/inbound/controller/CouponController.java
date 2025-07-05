package com.events.api.adapters.inbound.controller;

import com.events.api.domain.coupon.Coupon;
import com.events.api.domain.coupon.CouponRequestDTO;
import com.events.api.application.services.CouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/event/{eventId}")
    public ResponseEntity<Coupon> addCouponService(@PathVariable UUID eventId, @RequestBody CouponRequestDTO couponRequestDTO){
        Coupon coupon = couponService.addCouponToEvent(eventId, couponRequestDTO);
        return ResponseEntity.ok(coupon);
    }
}
