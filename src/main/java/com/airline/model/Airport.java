package com.airline.model;

import java.util.ArrayList;
import java.util.List;

public class Airport {

    private String name;
    private String city;
    private String description;
    private final List<Flight> departingFlights;
    private final List<Flight> arrivingFlights;

    public Airport(String name, String city, String description) {
        this.name = name;
        this.city = city;
        this.description = description;
        this.departingFlights = new ArrayList<>();
        this.arrivingFlights = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void assignDepartingFlight(Flight flight) {
        if (!departingFlights.contains(flight)) {
            departingFlights.add(flight);
        }
    }

    public void assignArrivingFlight(Flight flight) {
        if (!arrivingFlights.contains(flight)) {
            arrivingFlights.add(flight);
        }
    }

    public List<Flight> getDepartingFlights() {
        return new ArrayList<>(departingFlights);
    }

    public List<Flight> getArrivingFlights() {
        return new ArrayList<>(arrivingFlights);
    }

    public String getInfos() {
        return "Airport: " + name
                + " | City: " + city
                + " | Description: " + description
                + " | Departures: " + departingFlights.size()
                + " | Arrivals: " + arrivingFlights.size();
    }
}
