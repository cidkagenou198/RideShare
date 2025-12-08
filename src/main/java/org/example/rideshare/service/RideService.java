package org.example.rideshare.service;

import org.example.rideshare.dto.CreateRideRequest;
import org.example.rideshare.model.Ride;
import org.example.rideshare.repository.RideRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideService {

    private final RideRepository rideRepository;

    public RideService(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    public Ride createRide(String userId, CreateRideRequest request) {
        System.out.println("Creating ride for user: " + userId + " from " + request.getPickupLocation());
        Ride ride = new Ride(
                userId,
                request.getPickupLocation(),
                request.getDropLocation(),
                "REQUESTED");
        return rideRepository.save(ride);
    }

    public List<Ride> getMyRides(String userId) {
        return rideRepository.findByUserId(userId);
    }

    public List<Ride> getPendingRides() {
        return rideRepository.findByStatus("REQUESTED");
    }

    public Ride acceptRide(String rideId, String driverId) {
        System.out.println("Driver " + driverId + " accepting ride " + rideId);
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new org.example.rideshare.exception.NotFoundException("Ride not found"));

        if (!"REQUESTED".equals(ride.getStatus())) {
            throw new org.example.rideshare.exception.BadRequestException("Ride is not in REQUESTED status");
        }

        ride.setDriverId(driverId);
        ride.setStatus("ACCEPTED");
        return rideRepository.save(ride);
    }

    public Ride completeRide(String rideId) {
        System.out.println("Completing ride: " + rideId);
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new org.example.rideshare.exception.NotFoundException("Ride not found"));

        if (!"ACCEPTED".equals(ride.getStatus())) {
            throw new org.example.rideshare.exception.BadRequestException("Ride must be ACCEPTED to complete");
        }

        ride.setStatus("COMPLETED");
        return rideRepository.save(ride);
    }
}
