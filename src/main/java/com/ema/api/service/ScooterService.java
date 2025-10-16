package com.ema.api.service;

import com.ema.api.model.Rating;
import com.ema.api.model.Ride;
import com.ema.api.model.Scooter;
import com.ema.api.model.User;
import com.ema.api.repository.RideRepository;
import com.ema.api.repository.ScooterRepository;
import com.ema.api.repository.UserRepository;
import com.ema.api.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ScooterService {

    private final ScooterRepository scooterRepository;
    private final RideRepository rideRepository;
    private final UserRepository userRepository;

    @Autowired
    public ScooterService(ScooterRepository scooterRepository, @Lazy RideRepository rideRepository, UserRepository userRepository) {
        this.scooterRepository = scooterRepository;
        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
    }

    public Scooter createScooter(Scooter scooter) {
        return scooterRepository.save(scooter);
    }

    public List<Scooter> getAllScooters() {
        return scooterRepository.findAll();
    }

    public Optional<Scooter> getScooterById(Long id) {
        return scooterRepository.findById(id);
    }

    public Scooter updateScooter(Long id, Scooter scooterDetails) {
        Scooter scooter = scooterRepository.findById(id).orElseThrow(() -> new RuntimeException("Scooter not found with id: " + id));
        scooter.setLocation(scooterDetails.getLocation());
        scooter.setBatteryLevel(scooterDetails.getBatteryLevel());
        scooter.setStatus(scooterDetails.getStatus());
        return scooterRepository.save(scooter);
    }

    public void deleteScooter(Long id) {
        scooterRepository.deleteById(id);
    }

    public List<Scooter> getAvailableScooters() {
        return scooterRepository.findByStatus("AVAILABLE");
    }

    @Transactional
    public Ride startRide(Long scooterId, UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        Scooter scooter = scooterRepository.findById(scooterId).orElseThrow(() -> new IllegalStateException("Scooter not found"));

        if (!"AVAILABLE".equals(scooter.getStatus())) {
            throw new IllegalStateException("Scooter is not available");
        }

        scooter.setStatus("IN_USE");
        scooterRepository.save(scooter);

        Ride ride = new Ride();
        ride.setScooter(scooter);
        ride.setUser(user);
        ride.setStartTime(LocalDateTime.now());

        return rideRepository.save(ride);
    }

    @Transactional
    public Ride endRide(Long rideId, int ratingValue) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new IllegalStateException("Ride not found"));

        if (ride.getEndTime() != null) {
            throw new IllegalStateException("Ride has already ended");
        }

        ride.setEndTime(LocalDateTime.now());

        Scooter scooter = ride.getScooter();
        scooter.setStatus("AVAILABLE");
        scooterRepository.save(scooter);

        Rating rating = new Rating();
        rating.setRating(ratingValue);
        rating.setRide(ride);
        ride.setRating(rating);

        updateScooterAverageRating(scooter);

        return rideRepository.save(ride);
    }

    public void updateScooterAverageRating(Scooter scooter) {
        List<Ride> rides = rideRepository.findByScooter(scooter);
        double averageRating = rides.stream()
                .map(Ride::getRating)
                .filter(Objects::nonNull) // Filter out rides without ratings
                .mapToInt(Rating::getRating)
                .average()
                .orElse(0.0);

        scooter.setAverageRating(averageRating);
        scooterRepository.save(scooter);
    }
}
