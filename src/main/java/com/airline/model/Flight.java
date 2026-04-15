package com.airline.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Flight {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private String flightNumber;
    private Airport origin;
    private Airport destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalDateTime;
    private FlightStatus status;
    private double basePrice;
    private Aircraft aircraft;
    private AirplanePilot pilot;
    private final List<StaffCabin> cabinCrew;
    private final List<Booking> bookings;

    public Flight(String flightNumber, Airport origin, Airport destination,
                  LocalDateTime departureTime, LocalDateTime arrivalDateTime, double basePrice) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalDateTime = arrivalDateTime;
        this.basePrice = basePrice;
        this.status = FlightStatus.SCHEDULED;
        this.cabinCrew = new ArrayList<>();
        this.bookings = new ArrayList<>();
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Airport getOrigin() {
        return origin;
    }

    public void setOrigin(Airport origin) {
        this.origin = origin;
    }

    public Airport getDestination() {
        return destination;
    }

    public void setDestination(Airport destination) {
        this.destination = destination;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalDateTime() {
        return arrivalDateTime;
    }

    public void setArrivalDateTime(LocalDateTime arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }

    public FlightStatus getStatus() {
        return status;
    }

    public void setStatus(FlightStatus status) {
        this.status = status;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public AirplanePilot getPilot() {
        return pilot;
    }

    public void setPilot(AirplanePilot pilot) {
        this.pilot = pilot;
    }

    public List<StaffCabin> getCabinCrew() {
        return new ArrayList<>(cabinCrew);
    }

    public void addCabinCrew(StaffCabin staff) {
        if (!cabinCrew.contains(staff)) {
            cabinCrew.add(staff);
        }
    }

    public void addBooking(Booking booking) {
        if (!bookings.contains(booking)) {
            bookings.add(booking);
        }
    }

    public List<Booking> getBookings() {
        return new ArrayList<>(bookings);
    }

    public void planFlight() {
        this.status = FlightStatus.SCHEDULED;
    }

    public void cancelFlight() {
        this.status = FlightStatus.CANCELLED;
        for (Booking booking : bookings) {
            booking.cancelReservation();
        }
    }

    public void modifyFlight(LocalDateTime departureTime, LocalDateTime arrivalDateTime, FlightStatus status) {
        this.departureTime = departureTime;
        this.arrivalDateTime = arrivalDateTime;
        this.status = status;
    }

    public List<Passenger> listingPassengers() {
        List<Passenger> passengers = new ArrayList<>();
        for (Booking booking : bookings) {
            if (booking.getStatus() != BookingStatus.CANCELLED
                    && !passengers.contains(booking.getPassenger())) {
                passengers.add(booking.getPassenger());
            }
        }
        return passengers;
    }

    public int getActiveBookingCount() {
        int count = 0;
        for (Booking booking : bookings) {
            if (booking.getStatus() != BookingStatus.CANCELLED) {
                count++;
            }
        }
        return count;
    }

    public String getInfos() {
        String aircraftInfo = aircraft == null ? "Not assigned" : aircraft.getRegistration();
        String pilotInfo = pilot == null ? "Not assigned" : pilot.getName();
        return "Flight " + flightNumber
                + " | " + origin.getCity() + " (" + origin.getName() + ")"
                + " -> " + destination.getCity() + " (" + destination.getName() + ")"
                + " | Departure: " + departureTime.format(FORMATTER)
                + " | Arrival: " + arrivalDateTime.format(FORMATTER)
                + " | Status: " + status
                + " | Price: " + basePrice
                + " | Aircraft: " + aircraftInfo
                + " | Pilot: " + pilotInfo
                + " | Cabin Crew: " + cabinCrew.size()
                + " | Passengers: " + listingPassengers().size();
    }
}
