package com.airline.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AirplanePilot extends Employee {

    private String licence;
    private int flightHours;
    private final List<Flight> flights;

    public AirplanePilot(String id, String name, String address, String contact,
                         String empNumber, LocalDate hiringDate, String licence, int flightHours) {
        super(id, name, address, contact, empNumber, hiringDate);
        this.licence = licence;
        this.flightHours = flightHours;
        this.flights = new ArrayList<>();
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public int getFlightHours() {
        return flightHours;
    }

    public void setFlightHours(int flightHours) {
        this.flightHours = flightHours;
    }

    @Override
    public String getRole() {
        return "Airplane Pilot";
    }

    public void assignFlight(Flight flight) {
        if (!flights.contains(flight)) {
            flights.add(flight);
        }
    }

    public List<Flight> obtainVol() {
        return new ArrayList<>(flights);
    }
}
