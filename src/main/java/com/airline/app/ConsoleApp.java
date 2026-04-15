package com.airline.app;

import com.airline.model.AirplanePilot;
import com.airline.model.Aircraft;
import com.airline.model.Airport;
import com.airline.model.Booking;
import com.airline.model.Employee;
import com.airline.model.Flight;
import com.airline.model.Passenger;
import com.airline.model.StaffCabin;
import com.airline.service.AirlineSystem;
import com.airline.service.StatisticsService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleApp {

    private static final DateTimeFormatter INPUT_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final AirlineSystem system;
    private final StatisticsService statistics;
    private final Scanner scanner;

    public ConsoleApp() {
        this.system = new AirlineSystem();
        this.statistics = new StatisticsService(system);
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        SampleData.load(system);
        boolean running = true;
        System.out.println("=========================================");
        System.out.println("     AIRLINE MANAGEMENT SYSTEM");
        System.out.println("=========================================");
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> listFlights();
                case "2" -> showFlightDetails();
                case "3" -> planNewFlight();
                case "4" -> cancelFlight();
                case "5" -> assignCrew();
                case "6" -> assignAircraft();
                case "7" -> bookFlight();
                case "8" -> cancelReservation();
                case "9" -> showReservation();
                case "10" -> listPassengersOfFlight();
                case "11" -> managePassengers();
                case "12" -> manageEmployees();
                case "13" -> manageAircrafts();
                case "14" -> showStatistics();
                case "0" -> running = false;
                default -> System.out.println("Invalid option. Please try again.");
            }
            System.out.println();
        }
        System.out.println("Goodbye.");
    }

    private void printMenu() {
        System.out.println("----------------- MENU -----------------");
        System.out.println("1.  List all flights");
        System.out.println("2.  Show flight details");
        System.out.println("3.  Plan a new flight");
        System.out.println("4.  Cancel a flight");
        System.out.println("5.  Assign crew to a flight");
        System.out.println("6.  Assign aircraft to a flight");
        System.out.println("7.  Book a flight");
        System.out.println("8.  Cancel a reservation");
        System.out.println("9.  Show a reservation");
        System.out.println("10. List passengers of a flight");
        System.out.println("11. Manage passengers");
        System.out.println("12. Manage employees");
        System.out.println("13. Manage aircraft");
        System.out.println("14. Statistics and reports");
        System.out.println("0.  Exit");
        System.out.print("Choose an option: ");
    }

    private void listFlights() {
        List<Flight> flights = system.getFlights();
        if (flights.isEmpty()) {
            System.out.println("No flights available.");
            return;
        }
        for (Flight flight : flights) {
            System.out.println(flight.getInfos());
        }
    }

    private void showFlightDetails() {
        System.out.print("Enter flight number: ");
        String number = scanner.nextLine().trim();
        Flight flight = system.getFlight(number);
        if (flight == null) {
            System.out.println("Flight not found.");
            return;
        }
        System.out.println(flight.getInfos());
        if (flight.getPilot() != null) {
            System.out.println("Pilot: " + flight.getPilot().getInfos());
        }
        List<StaffCabin> crew = flight.getCabinCrew();
        if (!crew.isEmpty()) {
            System.out.println("Cabin crew:");
            for (StaffCabin staff : crew) {
                System.out.println("   " + staff.getInfos());
            }
        }
    }

    private void planNewFlight() {
        System.out.print("Flight number: ");
        String number = scanner.nextLine().trim();
        if (system.getFlight(number) != null) {
            System.out.println("A flight with this number already exists.");
            return;
        }
        Airport origin = readAirport("origin");
        if (origin == null) {
            return;
        }
        Airport destination = readAirport("destination");
        if (destination == null) {
            return;
        }
        LocalDateTime departure = readDateTime("departure (yyyy-MM-dd HH:mm): ");
        if (departure == null) {
            return;
        }
        LocalDateTime arrival = readDateTime("arrival (yyyy-MM-dd HH:mm): ");
        if (arrival == null) {
            return;
        }
        double price = readDouble("Base price: ");
        Flight flight = new Flight(number, origin, destination, departure, arrival, price);
        List<Flight> toPlan = new ArrayList<>();
        toPlan.add(flight);
        system.planFlight(toPlan);
        System.out.println("Flight planned successfully.");
    }

    private void cancelFlight() {
        System.out.print("Enter flight number to cancel: ");
        String number = scanner.nextLine().trim();
        if (system.cancelFlight(number)) {
            System.out.println("Flight cancelled. Related reservations were cancelled.");
        } else {
            System.out.println("Flight not found.");
        }
    }

    private void assignCrew() {
        System.out.print("Flight number: ");
        String number = scanner.nextLine().trim();
        if (system.getFlight(number) == null) {
            System.out.println("Flight not found.");
            return;
        }
        System.out.println("Available pilots:");
        for (Employee employee : system.getEmployees()) {
            if (employee instanceof AirplanePilot) {
                System.out.println("   " + employee.getId() + " - " + employee.getName());
            }
        }
        System.out.print("Pilot id: ");
        String pilotId = scanner.nextLine().trim();
        System.out.println("Available cabin crew:");
        for (Employee employee : system.getEmployees()) {
            if (employee instanceof StaffCabin) {
                System.out.println("   " + employee.getId() + " - " + employee.getName());
            }
        }
        System.out.print("Cabin crew ids (comma separated): ");
        String cabinLine = scanner.nextLine().trim();
        List<String> cabinIds = new ArrayList<>();
        for (String part : cabinLine.split(",")) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                cabinIds.add(trimmed);
            }
        }
        if (system.assignFlight(number, pilotId, cabinIds)) {
            System.out.println("Crew assigned successfully.");
        } else {
            System.out.println("Crew assignment failed. Check the pilot id and at least one valid cabin crew id.");
        }
    }

    private void assignAircraft() {
        System.out.print("Flight number: ");
        String number = scanner.nextLine().trim();
        Flight flight = system.getFlight(number);
        if (flight == null) {
            System.out.println("Flight not found.");
            return;
        }
        System.out.println("Available aircraft for this flight:");
        List<Aircraft> available = system.getAvailableAircrafts(flight);
        if (available.isEmpty()) {
            System.out.println("No aircraft available for this schedule.");
            return;
        }
        for (Aircraft aircraft : available) {
            System.out.println("   " + aircraft.getInfos());
        }
        System.out.print("Aircraft registration: ");
        String registration = scanner.nextLine().trim();
        if (system.affectFlight(number, registration)) {
            System.out.println("Aircraft assigned successfully.");
        } else {
            System.out.println("Assignment failed. The aircraft may be unavailable for this schedule.");
        }
    }

    private void bookFlight() {
        System.out.print("Passenger id: ");
        String passengerId = scanner.nextLine().trim();
        if (system.getPassenger(passengerId) == null) {
            System.out.println("Passenger not found.");
            return;
        }
        System.out.print("Flight number: ");
        String number = scanner.nextLine().trim();
        Booking booking = system.bookFlight(passengerId, number);
        if (booking == null) {
            System.out.println("Booking failed. Flight may be full, cancelled or not found.");
            return;
        }
        System.out.print("Confirm reservation now? (y/n): ");
        String confirm = scanner.nextLine().trim();
        if (confirm.equalsIgnoreCase("y")) {
            booking.confirmReservation();
        }
        System.out.println("Booking created: " + booking.getInfos());
    }

    private void cancelReservation() {
        System.out.print("Reservation number: ");
        String number = scanner.nextLine().trim();
        if (system.cancelBook(number)) {
            System.out.println("Reservation cancelled.");
        } else {
            System.out.println("Reservation not found.");
        }
    }

    private void showReservation() {
        System.out.print("Reservation number: ");
        String number = scanner.nextLine().trim();
        Booking booking = system.getReservation(number);
        if (booking == null) {
            System.out.println("Reservation not found.");
            return;
        }
        System.out.println(booking.getInfos());
    }

    private void listPassengersOfFlight() {
        System.out.print("Flight number: ");
        String number = scanner.nextLine().trim();
        Flight flight = system.getFlight(number);
        if (flight == null) {
            System.out.println("Flight not found.");
            return;
        }
        List<Passenger> passengers = flight.listingPassengers();
        if (passengers.isEmpty()) {
            System.out.println("No passengers on this flight.");
            return;
        }
        for (Passenger passenger : passengers) {
            System.out.println(passenger.getInfos());
        }
    }

    private void managePassengers() {
        System.out.println("1. List passengers");
        System.out.println("2. Add passenger");
        System.out.println("3. Update passenger");
        System.out.println("4. Delete passenger");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> {
                for (Passenger passenger : system.getPassengers()) {
                    System.out.println(passenger.getInfos());
                }
            }
            case "2" -> {
                System.out.print("Id: ");
                String id = scanner.nextLine().trim();
                System.out.print("Name: ");
                String name = scanner.nextLine().trim();
                System.out.print("Address: ");
                String address = scanner.nextLine().trim();
                System.out.print("Contact: ");
                String contact = scanner.nextLine().trim();
                System.out.print("Passport: ");
                String passport = scanner.nextLine().trim();
                system.addPassenger(new Passenger(id, name, address, contact, passport));
                System.out.println("Passenger added.");
            }
            case "3" -> {
                System.out.print("Id: ");
                String id = scanner.nextLine().trim();
                System.out.print("Name: ");
                String name = scanner.nextLine().trim();
                System.out.print("Address: ");
                String address = scanner.nextLine().trim();
                System.out.print("Contact: ");
                String contact = scanner.nextLine().trim();
                System.out.print("Passport: ");
                String passport = scanner.nextLine().trim();
                if (system.updatePassenger(id, name, address, contact, passport)) {
                    System.out.println("Passenger updated.");
                } else {
                    System.out.println("Passenger not found.");
                }
            }
            case "4" -> {
                System.out.print("Id: ");
                String id = scanner.nextLine().trim();
                if (system.removePassenger(id)) {
                    System.out.println("Passenger deleted.");
                } else {
                    System.out.println("Passenger not found.");
                }
            }
            default -> System.out.println("Invalid option.");
        }
    }

    private void manageEmployees() {
        System.out.println("1. List employees");
        System.out.println("2. Show role by id");
        System.out.println("3. Add pilot");
        System.out.println("4. Add cabin crew");
        System.out.println("5. Delete employee");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> {
                for (Employee employee : system.getEmployees()) {
                    System.out.println(employee.getInfos());
                }
            }
            case "2" -> {
                System.out.print("Employee id: ");
                String id = scanner.nextLine().trim();
                System.out.println("Role: " + system.getRole(id));
            }
            case "3" -> {
                System.out.print("Id: ");
                String id = scanner.nextLine().trim();
                System.out.print("Name: ");
                String name = scanner.nextLine().trim();
                System.out.print("Address: ");
                String address = scanner.nextLine().trim();
                System.out.print("Contact: ");
                String contact = scanner.nextLine().trim();
                System.out.print("Employee number: ");
                String empNumber = scanner.nextLine().trim();
                System.out.print("Licence: ");
                String licence = scanner.nextLine().trim();
                int hours = (int) readDouble("Flight hours: ");
                system.addEmployee(new AirplanePilot(id, name, address, contact, empNumber,
                        LocalDate.now(), licence, hours));
                System.out.println("Pilot added.");
            }
            case "4" -> {
                System.out.print("Id: ");
                String id = scanner.nextLine().trim();
                System.out.print("Name: ");
                String name = scanner.nextLine().trim();
                System.out.print("Address: ");
                String address = scanner.nextLine().trim();
                System.out.print("Contact: ");
                String contact = scanner.nextLine().trim();
                System.out.print("Employee number: ");
                String empNumber = scanner.nextLine().trim();
                System.out.print("Qualification: ");
                String qualification = scanner.nextLine().trim();
                system.addEmployee(new StaffCabin(id, name, address, contact, empNumber,
                        LocalDate.now(), qualification));
                System.out.println("Cabin crew added.");
            }
            case "5" -> {
                System.out.print("Employee id: ");
                String id = scanner.nextLine().trim();
                if (system.removeEmployee(id)) {
                    System.out.println("Employee deleted.");
                } else {
                    System.out.println("Employee not found.");
                }
            }
            default -> System.out.println("Invalid option.");
        }
    }

    private void manageAircrafts() {
        System.out.println("1. List aircraft");
        System.out.println("2. Add aircraft");
        System.out.println("3. Update aircraft");
        System.out.println("4. Delete aircraft");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> {
                for (Aircraft aircraft : system.getAircrafts()) {
                    System.out.println(aircraft.getInfos());
                }
            }
            case "2" -> {
                System.out.print("Registration: ");
                String registration = scanner.nextLine().trim();
                System.out.print("Model: ");
                String model = scanner.nextLine().trim();
                int capacity = (int) readDouble("Capacity: ");
                system.addAircraft(new Aircraft(registration, model, capacity));
                System.out.println("Aircraft added.");
            }
            case "3" -> {
                System.out.print("Registration: ");
                String registration = scanner.nextLine().trim();
                System.out.print("Model: ");
                String model = scanner.nextLine().trim();
                int capacity = (int) readDouble("Capacity: ");
                if (system.updateAircraft(registration, model, capacity)) {
                    System.out.println("Aircraft updated.");
                } else {
                    System.out.println("Aircraft not found.");
                }
            }
            case "4" -> {
                System.out.print("Registration: ");
                String registration = scanner.nextLine().trim();
                if (system.removeAircraft(registration)) {
                    System.out.println("Aircraft deleted.");
                } else {
                    System.out.println("Aircraft not found.");
                }
            }
            default -> System.out.println("Invalid option.");
        }
    }

    private void showStatistics() {
        System.out.println(statistics.generateReport());
    }

    private Airport readAirport(String label) {
        System.out.println("Available airports:");
        for (Airport airport : system.getAirports()) {
            System.out.println("   " + airport.getName() + " - " + airport.getCity());
        }
        System.out.print("Enter " + label + " airport name: ");
        String name = scanner.nextLine().trim();
        Airport airport = system.getAirport(name);
        if (airport == null) {
            System.out.println("Airport not found.");
        }
        return airport;
    }

    private LocalDateTime readDateTime(String label) {
        System.out.print(label);
        String value = scanner.nextLine().trim();
        try {
            return LocalDateTime.parse(value, INPUT_FORMAT);
        } catch (Exception e) {
            System.out.println("Invalid date format.");
            return null;
        }
    }

    private double readDouble(String label) {
        System.out.print(label);
        String value = scanner.nextLine().trim();
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number, using 0.");
            return 0;
        }
    }
}
