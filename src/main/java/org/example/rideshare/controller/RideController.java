package org.example.rideshare.controller;

import jakarta.validation.Valid;
import org.example.rideshare.dto.CreateRideRequest;
import org.example.rideshare.model.Ride;
import org.example.rideshare.model.User;
import org.example.rideshare.repository.UserRepository;
import org.example.rideshare.service.RideService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RideController {

    private final RideService rideService;
    private final UserRepository userRepository;

    public RideController(RideService rideService, UserRepository userRepository) {
        this.rideService = rideService;
        this.userRepository = userRepository;
    }

    private String getCurrentUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new org.example.rideshare.exception.NotFoundException("User not found"));
    }

    @PostMapping("/rides")
    public ResponseEntity<Ride> createRide(@RequestBody @Valid CreateRideRequest request) {
        return ResponseEntity.ok(rideService.createRide(getCurrentUserId(), request));
    }

    @GetMapping("/user/rides")
    public ResponseEntity<List<Ride>> getMyRides() {
        return ResponseEntity.ok(rideService.getMyRides(getCurrentUserId()));
    }

    @PostMapping("/rides/{id}/complete")
    public ResponseEntity<Ride> completeRide(@PathVariable String id) {
        return ResponseEntity.ok(rideService.completeRide(id));
    }
}
