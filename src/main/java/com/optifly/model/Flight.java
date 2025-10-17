package com.optifly.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "flights")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_id")
    private Long flightId;

    @Column(name = "source")
    private String source;

    @Column(name = "destination")
    private String destination;

    @Column(name = "flight_date")
    private LocalDateTime flightDate;

    @Column(name = "landing_date")
    private LocalDateTime landingDate;

    @Column(name = "eco_cl_cost")
    private Double ecoClCost;

    @Column(name = "bussi_cl_cost")
    private Double bussiClCost;

    @Column(name = "first_cl_cost")
    private Double firstClCost;

    // Constructors
    public Flight() {}

    // Constructor without flightId for new flights
    public Flight(String source, String destination,
                  LocalDateTime flightDate, LocalDateTime landingDate,
                  Double ecoClCost, Double bussiClCost, Double firstClCost) {
        this.source = source;
        this.destination = destination;
        this.flightDate = flightDate;
        this.landingDate = landingDate;
        this.ecoClCost = ecoClCost;
        this.bussiClCost = bussiClCost;
        this.firstClCost = firstClCost;
    }

    // Getters and Setters
    public Long getFlightId() { return flightId; }
    public void setFlightId(Long flightId) { this.flightId = flightId; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public LocalDateTime getFlightDate() { return flightDate; }
    public void setFlightDate(LocalDateTime flightDate) { this.flightDate = flightDate; }

    public LocalDateTime getLandingDate() { return landingDate; }
    public void setLandingDate(LocalDateTime landingDate) { this.landingDate = landingDate; }

    public Double getEcoClCost() { return ecoClCost; }
    public void setEcoClCost(Double ecoClCost) { this.ecoClCost = ecoClCost; }

    public Double getBussiClCost() { return bussiClCost; }
    public void setBussiClCost(Double bussiClCost) { this.bussiClCost = bussiClCost; }

    public Double getFirstClCost() { return firstClCost; }
    public void setFirstClCost(Double firstClCost) { this.firstClCost = firstClCost; }
}