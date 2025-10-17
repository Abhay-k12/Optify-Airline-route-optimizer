package com.optifly.service;

import com.optifly.backend.OptimisePath;
import com.optifly.backend.ReturnObject;
import com.optifly.backend.AirPortsCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RouteOptimizationService {

    @Autowired
    private OptimisePath optimisePath;

    @Autowired
    private AirPortsCodes airPortsCodes;

    public String findOptimalPath(String source, String destination, String priority) {
        try {
            ReturnObject path = optimisePath.shortestPath(source, destination, priority);

            if (path.distance.isEmpty()) {
                return "No valid route found from " + source + " to " + destination;
            }

            StringBuilder result = new StringBuilder();
            result.append("Optimized Route:\n\n");

            for (int i = 0; i < path.distance.size(); i++) {
                String airport = path.distance.get(i)[0];
                String flightId = path.distance.get(i)[1];

                // Use the directly injected airPortsCodes
                String city = airPortsCodes.getPortCity(
                        airPortsCodes.getAirportCode(airport)
                );

                if (city != null) {
                    city = city.substring(0, 1).toUpperCase() + city.substring(1);
                } else {
                    city = "Unknown";
                }

                result.append(airport.toUpperCase() + " {St: " + city);

                if (!flightId.equals("-1")) {
                    result.append(", FID: " + flightId + "}");
                } else {
                    result.append("}");
                }

                if (i != path.distance.size() - 1) result.append(" --> ");
            }

            result.append("\n\nTotal Economical Class Cost = " + String.format("%.2f", path.ecoClcost));
            result.append("\nTotal Business Class Cost = " + String.format("%.2f", path.busiClCost));
            result.append("\nTotal First Class Cost = " + String.format("%.2f", path.firstClCost));
            result.append("\nTotal Duration of Flight = " + String.format("%.2f", path.time) + " hours");

            return result.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error finding optimal path: " + e.getMessage();
        }
    }
}