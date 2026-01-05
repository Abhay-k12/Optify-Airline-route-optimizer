package com.optifly.controller;

import com.optifly.entity.Flight;
import com.optifly.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/flights")
@CrossOrigin(origins = "*")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @GetMapping("/{flightId}")
    public ResponseEntity<Flight> getFlightById(@PathVariable Long flightId) {
        Flight flight = flightService.getFlightById(flightId);
        if (flight != null) {
            return ResponseEntity.ok(flight);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> addFlight(@RequestBody FlightRequest request) {
        try {
            // Validate required fields
            if (request.getSource() == null || request.getSource().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Error: Source is required");
            }
            if (request.getDestination() == null || request.getDestination().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Error: Destination is required");
            }
            if (request.getFlightDate() == null || request.getFlightDate().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Error: Flight date is required");
            }
            if (request.getLandingDate() == null || request.getLandingDate().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Error: Landing date is required");
            }

            // Parse dates to LocalDateTime
            LocalDateTime flightDate = parseDateTime(request.getFlightDate());
            LocalDateTime landingDate = parseDateTime(request.getLandingDate());

            // Create flight - database will auto-generate flight_id
            Flight flight = new Flight(
                    request.getSource().toLowerCase().trim(),
                    request.getDestination().toLowerCase().trim(),
                    flightDate,
                    landingDate,
                    request.getEcoCost(),
                    request.getBusinessCost(),
                    request.getFirstCost()
            );

            Flight savedFlight = flightService.addFlight(flight);
            return ResponseEntity.ok("Flight from " + request.getSource() + " to " + request.getDestination() + " added successfully! Flight ID: " + savedFlight.getFlightId());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{flightId}")
    public ResponseEntity<String> updateFlight(@PathVariable Long flightId, @RequestBody FlightRequest request) {
        try {
            Flight existingFlight = flightService.getFlightById(flightId);
            if (existingFlight == null) {
                return ResponseEntity.notFound().build();
            }

            // Update only the fields that are provided
            if (request.getSource() != null && !request.getSource().trim().isEmpty()) {
                existingFlight.setSource(request.getSource().toLowerCase().trim());
            }
            if (request.getDestination() != null && !request.getDestination().trim().isEmpty()) {
                existingFlight.setDestination(request.getDestination().toLowerCase().trim());
            }
            if (request.getFlightDate() != null && !request.getFlightDate().trim().isEmpty()) {
                existingFlight.setFlightDate(parseDateTime(request.getFlightDate()));
            }
            if (request.getLandingDate() != null && !request.getLandingDate().trim().isEmpty()) {
                existingFlight.setLandingDate(parseDateTime(request.getLandingDate()));
            }
            if (request.getEcoCost() != null) {
                existingFlight.setEcoClCost(request.getEcoCost());
            }
            if (request.getBusinessCost() != null) {
                existingFlight.setBussiClCost(request.getBusinessCost());
            }
            if (request.getFirstCost() != null) {
                existingFlight.setFirstClCost(request.getFirstCost());
            }

            flightService.updateFlight(flightId, existingFlight);
            return ResponseEntity.ok("Flight updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{flightId}")
    public ResponseEntity<String> deleteFlight(@PathVariable Long flightId) {
        try {
            boolean deleted = flightService.deleteFlight(flightId);
            if (deleted) {
                return ResponseEntity.ok("Flight deleted successfully.");
            } else {
                return ResponseEntity.badRequest().body("Error: Flight not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Helper method to parse date with or without time
    private LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return null;
        }

        try {
            // Handle different date formats
            if (dateTimeStr.contains("T")) {
                // Format: 2025-10-16T10:00:00
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                return LocalDateTime.parse(dateTimeStr, formatter);
            } else if (dateTimeStr.contains(" ")) {
                // Format: 2025-10-16 10:00:00
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                return LocalDateTime.parse(dateTimeStr, formatter);
            } else {
                // Format: 2025-10-16 (date only) - add default time
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                return LocalDateTime.parse(dateTimeStr + "T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format: " + dateTimeStr + ". Expected format: yyyy-MM-dd or yyyy-MM-ddTHH:mm:ss", e);
        }
    }

    // Request DTO
    public static class FlightRequest {
        private String source;
        private String destination;
        private String flightDate;
        private String landingDate;
        private Double ecoCost;
        private Double businessCost;
        private Double firstCost;

        // Getters and Setters
        public String getSource() { return source; }
        public void setSource(String source) { this.source = source; }

        public String getDestination() { return destination; }
        public void setDestination(String destination) { this.destination = destination; }

        public String getFlightDate() { return flightDate; }
        public void setFlightDate(String flightDate) { this.flightDate = flightDate; }

        public String getLandingDate() { return landingDate; }
        public void setLandingDate(String landingDate) { this.landingDate = landingDate; }

        public Double getEcoCost() { return ecoCost; }
        public void setEcoCost(Double ecoCost) { this.ecoCost = ecoCost; }

        public Double getBusinessCost() { return businessCost; }
        public void setBusinessCost(Double businessCost) { this.businessCost = businessCost; }

        public Double getFirstCost() { return firstCost; }
        public void setFirstCost(Double firstCost) { this.firstCost = firstCost; }
    }
}