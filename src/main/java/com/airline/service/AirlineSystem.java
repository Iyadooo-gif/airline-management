package com.airline.service;

import com.airline.model.AirplanePilot;
import com.airline.model.Aircraft;
import com.airline.model.Airport;
import com.airline.model.Booking;
import com.airline.model.Employee;
import com.airline.model.Flight;
import com.airline.model.FlightStatus;
import com.airline.model.Passenger;
import com.airline.model.StaffCabin;

import java.util.ArrayList;
import java.util.List;

public class AirlineSystem {

    private final List<Airport> airports = new ArrayList<>();
    private final List<Aircraft> aircrafts = new ArrayList<>();
    private final List<Flight> flights = new ArrayList<>();
    private final List<Passenger> passengers = new ArrayList<>();
    private final List<Employee> employees = new ArrayList<>();
    private final List<Booking> bookings = new ArrayList<>();

    private int reservationCounter = 1000;

    public void addAirport(Airport airport) {
        airports.add(airport);
    }

    public Airport getAirport(String name) {
        for (Airport airport : airports) {
            if (airport.getName().equalsIgnoreCase(name)) {
                return airport;
            }
        }
        return null;
    }

    public boolean removeAirport(String name) {
        return airports.removeIf(airport -> airport.getName().equalsIgnoreCase(name));
    }

    public List<Airport> getAirports() {
        return new ArrayList<>(airports);
    }

    public void addAircraft(Aircraft aircraft) {
        aircrafts.add(aircraft);
    }

    public Aircraft getAircraft(String registration) {
        for (Aircraft aircraft : aircrafts) {
            if (aircraft.getRegistration().equalsIgnoreCase(registration)) {
                return aircraft;
            }
        }
        return null;
    }

    public boolean updateAircraft(String registration, String model, int capacity) {
        Aircraft aircraft = getAircraft(registration);
        if (aircraft == null) {
            return false;
        }
        aircraft.setModel(model);
        aircraft.setCapacity(capacity);
        return true;
    }

    public boolean removeAircraft(String registration) {
        return aircrafts.removeIf(aircraft -> aircraft.getRegistration().equalsIgnoreCase(registration));
    }

    public List<Aircraft> getAircrafts() {
        return new ArrayList<>(aircrafts);
    }

    public List<Aircraft> getAvailableAircrafts(Flight flight) {
        List<Aircraft> available = new ArrayList<>();
        for (Aircraft aircraft : aircrafts) {
            if (aircraft.checkAvailability(flight.getDepartureTime(), flight.getArrivalDateTime())) {
                available.add(aircraft);
            }
        }
        return available;
    }

    public void addFlight(Flight flight) {
        flights.add(flight);
        flight.getOrigin().assignDepartingFlight(flight);
        flight.getDestination().assignArrivingFlight(flight);
    }

    public Flight getFlight(String flightNumber) {
        for (Flight flight : flights) {
            if (flight.getFlightNumber().equalsIgnoreCase(flightNumber)) {
                return flight;
            }
        }
        return null;
    }

    public boolean removeFlight(String flightNumber) {
        return flights.removeIf(flight -> flight.getFlightNumber().equalsIgnoreCase(flightNumber));
    }

    public List<Flight> getFlights() {
        return new ArrayList<>(flights);
    }

    public void planFlight(List<Flight> newFlights) {
        for (Flight flight : newFlights) {
            flight.planFlight();
            addFlight(flight);
        }
    }

    public boolean cancelFlight(String flightNumber) {
        Flight flight = getFlight(flightNumber);
        if (flight == null) {
            return false;
        }
        flight.cancelFlight();
        return true;
    }

    public boolean affectFlight(String flightNumber, String registration) {
        Flight flight = getFlight(flightNumber);
        Aircraft aircraft = getAircraft(registration);
        if (flight == null || aircraft == null) {
            return false;
        }
        if (!aircraft.checkAvailability(flight.getDepartureTime(), flight.getArrivalDateTime())) {
            return false;
        }
        aircraft.assignFlight(flight);
        flight.setAircraft(aircraft);
        return true;
    }

    public boolean assignFlight(String flightNumber, String pilotId, List<String> cabinIds) {
        Flight flight = getFlight(flightNumber);
        if (flight == null) {
            return false;
        }
        Employee pilotEmployee = getEmployee(pilotId);
        if (!(pilotEmployee instanceof AirplanePilot)) {
            return false;
        }
        AirplanePilot pilot = (AirplanePilot) pilotEmployee;
        flight.setPilot(pilot);
        pilot.assignFlight(flight);
        for (String cabinId : cabinIds) {
            Employee cabinEmployee = getEmployee(cabinId);
            if (cabinEmployee instanceof StaffCabin) {
                StaffCabin staff = (StaffCabin) cabinEmployee;
                flight.addCabinCrew(staff);
                staff.assignFlight(flight);
            }
        }
        return flight.getPilot() != null && !flight.getCabinCrew().isEmpty();
    }

    public void addPassenger(Passenger passenger) {
        passengers.add(passenger);
    }

    public Passenger getPassenger(String id) {
        for (Passenger passenger : passengers) {
            if (passenger.getId().equalsIgnoreCase(id)) {
                return passenger;
            }
        }
        return null;
    }

    public boolean updatePassenger(String id, String name, String address, String contact, String passport) {
        Passenger passenger = getPassenger(id);
        if (passenger == null) {
            return false;
        }
        passenger.setName(name);
        passenger.setAddress(address);
        passenger.setContact(contact);
        passenger.setPassport(passport);
        return true;
    }

    public boolean removePassenger(String id) {
        return passengers.removeIf(passenger -> passenger.getId().equalsIgnoreCase(id));
    }

    public List<Passenger> getPassengers() {
        return new ArrayList<>(passengers);
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public Employee getEmployee(String id) {
        for (Employee employee : employees) {
            if (employee.getId().equalsIgnoreCase(id)) {
                return employee;
            }
        }
        return null;
    }

    public boolean removeEmployee(String id) {
        return employees.removeIf(employee -> employee.getId().equalsIgnoreCase(id));
    }

    public List<Employee> getEmployees() {
        return new ArrayList<>(employees);
    }

    public String getRole(String employeeId) {
        Employee employee = getEmployee(employeeId);
        if (employee == null) {
            return "Unknown";
        }
        return employee.getRole();
    }

    public Booking bookFlight(String passengerId, String flightNumber) {
        Passenger passenger = getPassenger(passengerId);
        Flight flight = getFlight(flightNumber);
        if (passenger == null || flight == null) {
            return null;
        }
        if (flight.getStatus() == FlightStatus.CANCELLED) {
            return null;
        }
        if (flight.getAircraft() != null
                && flight.getActiveBookingCount() >= flight.getAircraft().getCapacity()) {
            return null;
        }
        String reservationNumber = generateReservationNumber();
        Booking booking = passenger.bookFlight(flight, reservationNumber);
        bookings.add(booking);
        return booking;
    }

    public boolean cancelBook(String reservationNumber) {
        Booking booking = getReservation(reservationNumber);
        if (booking == null) {
            return false;
        }
        booking.cancelReservation();
        return true;
    }

    public boolean confirmBook(String reservationNumber) {
        Booking booking = getReservation(reservationNumber);
        if (booking == null) {
            return false;
        }
        booking.confirmReservation();
        return true;
    }

    public Booking getReservation(String reservationNumber) {
        for (Booking booking : bookings) {
            if (booking.getReservationNumber().equalsIgnoreCase(reservationNumber)) {
                return booking;
            }
        }
        return null;
    }

    public List<Booking> getBookings() {
        return new ArrayList<>(bookings);
    }

    private String generateReservationNumber() {
        reservationCounter++;
        return "R" + reservationCounter;
    }
}
