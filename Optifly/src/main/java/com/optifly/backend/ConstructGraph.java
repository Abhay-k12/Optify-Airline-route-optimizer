package com.optifly.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class ConstructGraph {

    @Autowired
    private AirPortsCodes codes;

    @Autowired
    private DataSource dataSource;

    public int ports;
    public List<List<List<Double>>> adj;

    public ConstructGraph() {
        this.codes = new AirPortsCodes();
        this.ports = codes.airPorts.length;
        this.adj = new ArrayList<>(ports);

        for (int i = 0; i < ports; i++) {
            adj.add(new ArrayList<>());
        }
    }

    @Autowired
    public ConstructGraph(AirPortsCodes airPortsCodes, DataSource dataSource) {
        this.codes = airPortsCodes;
        this.dataSource = dataSource;
        this.ports = codes.airPorts.length;
        this.adj = new ArrayList<>(ports);

        for (int i = 0; i < ports; i++) {
            adj.add(new ArrayList<>());
        }
    }

    private void addEdge(int src, int dst, double ecoCost, double busiCost, double fstCost, double duration, double flightId) {
        if (src >= 0 && src < ports && dst >= 0 && dst < ports) {
            List<Double> newEdge = Arrays.asList((double) dst, ecoCost, busiCost, fstCost, duration, flightId);
            adj.get(src).add(newEdge);
        } else {
            System.err.println("Invalid airport code for edge: src=" + src + " dst=" + dst);
        }
    }

    public void buildGraph(final Connection con) throws SQLException {
        // Clear existing graph
        for (int i = 0; i < ports; i++) {
            adj.get(i).clear();
        }

        // Updated SQL query with correct column names
        String sql = "SELECT flight_id, source, destination, flight_date, landing_date, eco_cl_cost, bussi_cl_cost, first_cl_cost FROM flights";

        try (PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                int src = codes.getAirportCode(rs.getString("source"));
                int dst = codes.getAirportCode(rs.getString("destination"));

                // Parse dates from the correct columns
                LocalDateTime flightDate = convertStrToDate(rs.getString("flight_date"));
                LocalDateTime landingDate = convertStrToDate(rs.getString("landing_date"));
                double duration = differenceBetweenDates(flightDate, landingDate);

                double ecoCost = rs.getDouble("eco_cl_cost");
                double busiCost = rs.getDouble("bussi_cl_cost");
                double fstClCost = rs.getDouble("first_cl_cost");
                double flightId = rs.getInt("flight_id");

                if (src != -1 && dst != -1) {
                    addEdge(src, dst, ecoCost, busiCost, fstClCost, duration, flightId);
                } else {
                    System.err.println("No such airport exists: source=" + rs.getString("source") + " destination=" + rs.getString("destination"));
                }
            }
        }
    }

    private LocalDateTime convertStrToDate(String date) {
        if (date == null) {
            return LocalDateTime.now();
        }

        try {
            // Handle different date formats from database
            if (date.contains("T")) {
                DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                return LocalDateTime.parse(date, pattern);
            } else {
                // MySQL DATETIME format: 2025-10-16 10:00:00
                DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                return LocalDateTime.parse(date, pattern);
            }
        } catch (Exception e) {
            System.err.println("Error parsing date string: " + date);
            return LocalDateTime.now();
        }
    }

    private double differenceBetweenDates(LocalDateTime start, LocalDateTime end) {
        Duration duration = Duration.between(start, end);
        return duration.toHours();
    }
}