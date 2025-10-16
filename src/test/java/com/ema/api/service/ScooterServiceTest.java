package com.ema.api.service;

import com.ema.api.model.Scooter;
import com.ema.api.repository.RideRepository;
import com.ema.api.repository.ScooterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScooterServiceTest {

    @Mock
    private ScooterRepository scooterRepository;

    @Mock
    private RideRepository rideRepository;

    @InjectMocks
    private ScooterService scooterService;

    private Scooter scooter;

    @BeforeEach
    void setUp() {
        scooter = new Scooter();
        scooter.setId(1L);
        scooter.setLocation("Test Location");
        scooter.setBatteryLevel(95);
        scooter.setStatus("AVAILABLE");
    }

    @Test
    void createScooter() {
        when(scooterRepository.save(any(Scooter.class))).thenReturn(scooter);

        Scooter createdScooter = scooterService.createScooter(new Scooter());

        assertNotNull(createdScooter);
        assertEquals(scooter.getId(), createdScooter.getId());
        verify(scooterRepository, times(1)).save(any(Scooter.class));
    }

    @Test
    void getAllScooters() {
        when(scooterRepository.findAll()).thenReturn(Arrays.asList(scooter));

        List<Scooter> scooters = scooterService.getAllScooters();

        assertNotNull(scooters);
        assertEquals(1, scooters.size());
    }

    @Test
    void getScooterById() {
        when(scooterRepository.findById(1L)).thenReturn(Optional.of(scooter));

        Optional<Scooter> foundScooter = scooterService.getScooterById(1L);

        assertTrue(foundScooter.isPresent());
        assertEquals(scooter.getId(), foundScooter.get().getId());
    }

    @Test
    void updateScooter() {
        when(scooterRepository.findById(1L)).thenReturn(Optional.of(scooter));
        when(scooterRepository.save(any(Scooter.class))).thenReturn(scooter);

        Scooter scooterDetails = new Scooter();
        scooterDetails.setLocation("New Location");

        Scooter updatedScooter = scooterService.updateScooter(1L, scooterDetails);

        assertNotNull(updatedScooter);
        assertEquals("New Location", updatedScooter.getLocation());
    }

    @Test
    void deleteScooter() {
        doNothing().when(scooterRepository).deleteById(1L);
        scooterService.deleteScooter(1L);
        verify(scooterRepository, times(1)).deleteById(1L);
    }

    @Test
    void getAvailableScooters() {
        when(scooterRepository.findByStatus("AVAILABLE")).thenReturn(Arrays.asList(scooter));

        List<Scooter> availableScooters = scooterService.getAvailableScooters();

        assertNotNull(availableScooters);
        assertFalse(availableScooters.isEmpty());
        assertEquals("AVAILABLE", availableScooters.get(0).getStatus());
    }

    // Test for updateScooterAverageRating will be added later as it requires more setup
}
