package com.ema.api.controller;

import com.ema.api.model.Scooter;
import com.ema.api.service.ScooterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/scooters")
public class ScooterController {

    private final ScooterService scooterService;

    @Autowired
    public ScooterController(ScooterService scooterService) {
        this.scooterService = scooterService;
    }

    @PostMapping
    public Scooter createScooter(@RequestBody Scooter scooter) {
        return scooterService.createScooter(scooter);
    }

    @GetMapping
    public List<Scooter> getAllScooters() {
        return scooterService.getAllScooters();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Scooter> getScooterById(@PathVariable Long id) {
        return scooterService.getScooterById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Scooter updateScooter(@PathVariable Long id, @RequestBody Scooter scooter) {
        return scooterService.updateScooter(id, scooter);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScooter(@PathVariable Long id) {
        scooterService.deleteScooter(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public List<Scooter> getAvailableScooters() {
        return scooterService.getAvailableScooters();
    }
}
