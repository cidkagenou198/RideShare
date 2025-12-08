package org.example.rideshare.controller;

import org.example.rideshare.model.Ride;
import org.example.rideshare.model.User;
import org.example.rideshare.repository.UserRepository;
import org.example.rideshare.service.RideService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/driver/rides")
public class DriverController {

    private final RideService rideService;
    private final UserRepository userRepository;

    public DriverController(RideService rideService, UserRepository userRepository) {
        this.rideService = rideService;
        this.userRepository = userRepository;
    }

    private String getCurrentDriverId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new org.example.rideshare.exception.NotFoundException("Driver not found"));
    }

    @GetMapping("/requests")
    public ResponseEntity<List<Ride>> getPendingRides() {
        return ResponseEntity.ok(rideService.getPendingRides());
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<Ride> acceptRide(@PathVariable String id) {
        return ResponseEntity.ok(rideService.acceptRide(id, getCurrentDriverId()));
    }
}
