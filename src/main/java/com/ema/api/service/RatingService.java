package com.ema.api.service;

import com.ema.api.model.Rating;
import com.ema.api.model.Ride;
import com.ema.api.model.Scooter;
import com.ema.api.repository.RatingRepository;
import com.ema.api.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final RideRepository rideRepository;
    private final ScooterService scooterService; // Inject ScooterService

    @Autowired
    public RatingService(RatingRepository ratingRepository, RideRepository rideRepository, ScooterService scooterService) {
        this.ratingRepository = ratingRepository;
        this.rideRepository = rideRepository;
        this.scooterService = scooterService;
    }

    public Rating createRating(Long rideId, int ratingValue, String comment) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found with id: " + rideId));

        if (!"COMPLETED".equals(ride.getStatus())) {
            throw new IllegalStateException("You can only rate completed rides.");
        }

        if (ratingRepository.findByRide(ride).isPresent()) {
            throw new IllegalStateException("This ride has already been rated.");
        }

        Rating newRating = new Rating(ride, ratingValue, comment);
        Rating savedRating = ratingRepository.save(newRating);

        // Update scooter's average rating
        scooterService.updateScooterAverageRating(ride.getScooter());

        return savedRating;
    }
}
