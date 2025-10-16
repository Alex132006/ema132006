package com.ema.api.repository;

import com.ema.api.model.Ride;
import com.ema.api.model.Scooter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
    List<Ride> findByScooter(Scooter scooter);
}
