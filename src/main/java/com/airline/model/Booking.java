package com.airline.model;

import java.time.LocalDate;

public class Booking {

    private String reservationNumber;
    private LocalDate dateReservation;
    private BookingStatus status;
    private Passenger passenger;
    private Flight flight;
    private double price;

    public Booking(String reservationNumber, LocalDate dateReservation, Passenger passenger, Flight flight) {
        this.reservationNumber = reservationNumber;
        this.dateReservation = dateReservation;
        this.passenger = passenger;
        this.flight = flight;
        this.price = flight.getBasePrice();
        this.status = BookingStatus.PENDING;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public LocalDate getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDate dateReservation) {
        this.dateReservation = dateReservation;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void confirmReservation() {
        this.status = BookingStatus.CONFIRMED;
    }

    public void cancelReservation() {
        this.status = BookingStatus.CANCELLED;
    }

    public void modifyReservation(Flight newFlight) {
        this.flight = newFlight;
        this.price = newFlight.getBasePrice();
    }

    public String getInfos() {
        return "Reservation: " + reservationNumber
                + " | Date: " + dateReservation
                + " | Status: " + status
                + " | Passenger: " + passenger.getName()
                + " | Flight: " + flight.getFlightNumber()
                + " | Price: " + price;
    }
}
