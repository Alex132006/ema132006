package com.ema.api.repository;

import com.ema.api.model.Rating;
import com.ema.api.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByRide(Ride ride);
}
