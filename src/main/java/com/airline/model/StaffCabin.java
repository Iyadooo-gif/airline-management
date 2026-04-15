package com.airline.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StaffCabin extends Employee {

    private String qualification;
    private final List<Flight> flights;

    public StaffCabin(String id, String name, String address, String contact,
                      String empNumber, LocalDate hiringDate, String qualification) {
        super(id, name, address, contact, empNumber, hiringDate);
        this.qualification = qualification;
        this.flights = new ArrayList<>();
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    @Override
    public String getRole() {
        return "Cabin Crew";
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
