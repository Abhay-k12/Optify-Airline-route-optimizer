package com.optifly.service;

import com.optifly.entity.Flight;
import com.optifly.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    public Flight getFlightById(Long flightId) {
        return flightRepository.findById(flightId).orElse(null);
    }

    public Flight addFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public Flight updateFlight(Long flightId, Flight flightDetails) {
        Flight existingFlight = flightRepository.findById(flightId).orElse(null);
        if (existingFlight != null) {
            // Update fields only if they are not null in flightDetails
            if (flightDetails.getSource() != null) {
                existingFlight.setSource(flightDetails.getSource());
            }
            if (flightDetails.getDestination() != null) {
                existingFlight.setDestination(flightDetails.getDestination());
            }
            if (flightDetails.getFlightDate() != null) {
                existingFlight.setFlightDate(flightDetails.getFlightDate());
            }
            if (flightDetails.getLandingDate() != null) {
                existingFlight.setLandingDate(flightDetails.getLandingDate());
            }
            if (flightDetails.getEcoClCost() != null) {
                existingFlight.setEcoClCost(flightDetails.getEcoClCost());
            }
            if (flightDetails.getBussiClCost() != null) {
                existingFlight.setBussiClCost(flightDetails.getBussiClCost());
            }
            if (flightDetails.getFirstClCost() != null) {
                existingFlight.setFirstClCost(flightDetails.getFirstClCost());
            }
            return flightRepository.save(existingFlight);
        }
        return null;
    }

    public boolean deleteFlight(Long flightId) {
        if (flightRepository.existsById(flightId)) {
            flightRepository.deleteById(flightId);
            return true;
        }
        return false;
    }
}