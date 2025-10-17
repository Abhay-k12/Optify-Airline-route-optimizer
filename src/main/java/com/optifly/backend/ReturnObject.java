package com.optifly.backend;

import java.util.*;

public class ReturnObject {
    public List<String[]> distance;
    public double ecoClcost;
    public double busiClCost;
    public double firstClCost;
    public double time;

    public ReturnObject(){
        distance = new ArrayList<>();
    }

    public ReturnObject(List<String[]> distance, double ecoClCost, double busiClCost, double firstClCost, double time) {
        this.distance = distance;
        this.ecoClcost = ecoClCost;
        this.busiClCost = busiClCost;
        this.firstClCost = firstClCost;
        this.time = time;
    }

    // Getters for JSON serialization
    public List<String[]> getDistance() { return distance; }
    public double getEcoClcost() { return ecoClcost; }
    public double getBusiClCost() { return busiClCost; }
    public double getFirstClCost() { return firstClCost; }
    public double getTime() { return time; }
}
