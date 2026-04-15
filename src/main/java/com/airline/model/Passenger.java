package com.airline.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Passenger extends Person {

    private String passport;
    private final List<Booking> bookings;

    public Passenger(String id, String name, String address, String contact, String passport) {
        super(id, name, address, contact);
        this.passport = passport;
        this.bookings = new ArrayList<>();
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public Booking bookFlight(Flight flight, String reservationNumber) {
        Booking booking = new Booking(reservationNumber, LocalDate.now(), this, flight);
        bookings.add(booking);
        flight.addBooking(booking);
        return booking;
    }

    public boolean cancelFlight(String reservationNumber) {
        for (Booking booking : bookings) {
            if (booking.getReservationNumber().equals(reservationNumber)) {
                booking.cancelReservation();
                return true;
            }
        }
        return false;
    }

    public List<Booking> getBooks() {
        return new ArrayList<>(bookings);
    }

    @Override
    public String getInfos() {
        return "ID: " + getId()
                + " | Name: " + getName()
                + " | Address: " + getAddress()
                + " | Contact: " + getContact()
                + " | Passport: " + passport
                + " | Bookings: " + bookings.size();
    }
}
