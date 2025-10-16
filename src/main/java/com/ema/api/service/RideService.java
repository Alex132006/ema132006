package com.ema.api.service;

import com.ema.api.model.Payment;
import com.ema.api.model.Ride;
import com.ema.api.model.Scooter;
import com.ema.api.model.User;
import com.ema.api.repository.RideRepository;
import com.ema.api.repository.ScooterRepository;
import com.ema.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RideService {

    private final RideRepository rideRepository;
    private final PaymentService paymentService;
    private final ScooterRepository scooterRepository;
    private final UserRepository userRepository;
    private static final BigDecimal BASE_FARE_FCFA = new BigDecimal("1500");
    private static final BigDecimal PRICE_PER_KILOMETER_FCFA = new BigDecimal("900");

    @Autowired
    public RideService(RideRepository rideRepository, PaymentService paymentService, ScooterRepository scooterRepository, UserRepository userRepository) {
        this.rideRepository = rideRepository;
        this.paymentService = paymentService;
        this.scooterRepository = scooterRepository;
        this.userRepository = userRepository;
    }

    public Ride startRide(Long userId, Long scooterId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Scooter scooter = scooterRepository.findById(scooterId).orElseThrow(() -> new RuntimeException("Scooter not found"));

        if (!"AVAILABLE".equals(scooter.getStatus())) {
            throw new IllegalStateException("Scooter is not available");
        }

        scooter.setStatus("IN_USE");
        scooterRepository.save(scooter);

        Ride ride = new Ride();
        ride.setUser(user);
        ride.setScooter(scooter);
        ride.setStartTime(LocalDateTime.now());
        ride.setPickupLocation(scooter.getLocation());
        ride.setStatus("IN_PROGRESS");

        return rideRepository.save(ride);
    }

    public Ride endRide(Long rideId, String dropoffLocation) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new RuntimeException("Ride not found"));

        ride.setDropoffLocation(dropoffLocation);
        ride.setEndTime(LocalDateTime.now());
        ride.setStatus("COMPLETED");

        // TODO: Calculate real distance
        ride.setDistance(5.0); // Dummy distance
        calculateAndSetPrice(ride);

        Scooter scooter = ride.getScooter();
        scooter.setLocation(dropoffLocation);
        scooter.setStatus("AVAILABLE");
        scooterRepository.save(scooter);

        createPaymentForRide(ride);

        return rideRepository.save(ride);
    }

    public List<Ride> getAllRides() {
        return rideRepository.findAll();
    }

    public Optional<Ride> getRideById(Long id) {
        return rideRepository.findById(id);
    }

    private void calculateAndSetPrice(Ride ride) {
        if (ride.getDistance() != null) {
            BigDecimal distance = BigDecimal.valueOf(ride.getDistance());
            BigDecimal price = BASE_FARE_FCFA.add(distance.multiply(PRICE_PER_KILOMETER_FCFA));
            ride.setPrice(price);
        }
    }

    private void createPaymentForRide(Ride ride) {
        Payment payment = new Payment();
        payment.setRide(ride);
        payment.setAmount(ride.getPrice());
        payment.setCurrency("FCFA");
        payment.setStatus("PENDING"); // Or trigger a payment gateway
        payment.setPaymentMethod("MOBILE_MONEY"); // Default or from user's profile
        paymentService.createPayment(payment);
    }
}
