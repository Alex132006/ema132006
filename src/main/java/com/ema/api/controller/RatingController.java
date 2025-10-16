package com.ema.api.controller;

import com.ema.api.controller.request.RatingRequest;
import com.ema.api.model.Rating;
import com.ema.api.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RatingController {

    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("/rides/{rideId}/ratings")
    public Rating createRating(@PathVariable Long rideId, @RequestBody RatingRequest ratingRequest) {
        return ratingService.createRating(rideId, ratingRequest.getRating(), ratingRequest.getComment());
    }
}
