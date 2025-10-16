package com.ema.api.controller;

import com.ema.api.model.Ride;
import com.ema.api.payload.StartRideRequest;
import com.ema.api.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    private final RideService rideService;

    @Autowired
    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping("/start")
    public Ride startRide(@RequestBody StartRideRequest startRideRequest) {
        return rideService.startRide(startRideRequest.getUserId(), startRideRequest.getScooterId());
    }

    @PostMapping("/{id}/end")
    public Ride endRide(@PathVariable Long id, @RequestBody String dropoffLocation) {
        return rideService.endRide(id, dropoffLocation);
    }

    @GetMapping
    public List<Ride> getAllRides() {
        return rideService.getAllRides();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ride> getRideById(@PathVariable Long id) {
        return rideService.getRideById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
