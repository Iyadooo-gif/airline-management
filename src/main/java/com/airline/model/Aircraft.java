package com.airline.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Aircraft {

    private String registration;
    private String model;
    private int capacity;
    private final List<Flight> assignedFlights;

    public Aircraft(String registration, String model, int capacity) {
        this.registration = registration;
        this.model = model;
        this.capacity = capacity;
        this.assignedFlights = new ArrayList<>();
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void assignFlight(Flight flight) {
        if (!assignedFlights.contains(flight)) {
            assignedFlights.add(flight);
        }
    }

    public boolean checkAvailability(LocalDateTime departure, LocalDateTime arrival) {
        for (Flight flight : assignedFlights) {
            if (flight.getStatus() == FlightStatus.CANCELLED) {
                continue;
            }
            boolean overlap = departure.isBefore(flight.getArrivalDateTime())
                    && flight.getDepartureTime().isBefore(arrival);
            if (overlap) {
                return false;
            }
        }
        return true;
    }

    public List<Flight> getAssignedFlights() {
        return new ArrayList<>(assignedFlights);
    }

    public String getInfos() {
        return "Registration: " + registration
                + " | Model: " + model
                + " | Capacity: " + capacity
                + " | Assigned Flights: " + assignedFlights.size();
    }
}
