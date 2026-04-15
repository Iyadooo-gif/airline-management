# Airline Management System

A Java-based airline reservation system built with object-oriented programming.
The application manages customers, crew, aircraft, flights, reservations and
cancellations, following the UML class diagram of the project.

Module: **II.1102 - JAVA Programming and Algorithms**

## Features

### Personal management
- `getInfos()` displays the information of any person (passenger or employee).
- `getRole()` returns the role of an employee (Airplane Pilot, Cabin Crew).

### Reservation management
- `bookFlight()` lets a passenger book one or several flights.
- `cancelBook()` cancels a reservation by its reservation number.
- `getReservation()` retrieves the information of a reservation by its number.

### Flight management
- `assignFlight()` assigns a crew (pilot + cabin crew) to a flight.
- `getFlight()` retrieves the information of a flight by its flight number.
- `planFlight()` plans a set of flights over a given period.
- `cancelFlight()` cancels a flight by its flight number.

### Aircraft management
- `affectFlight()` assigns an available aircraft to a flight.
- `checkAvailability()` checks the availability of an aircraft against the schedule.

### CRUD
Each entity (Airport, Aircraft, Flight, Passenger, Employee, Booking) supports
adding, modifying, retrieving and deleting instances.

### Statistics and reports (bonus)
- Number of flights, passengers carried and revenue generated.
- Most popular destinations.

## Object-oriented design

- **Inheritance**: `Person` -> `Employee` / `Passenger`, and `Employee` ->
  `AirplanePilot` / `StaffCabin`.
- **Encapsulation**: all fields are private and exposed through getters/setters.
- **Association**: `Flight` is linked to `Airport`, `Aircraft`, `AirplanePilot`
  and `StaffCabin`.
- **Composition**: a `Passenger` owns its list of `Booking` objects, a `Flight`
  owns its list of bookings and crew.

## Project structure

```
src/main/java/com/airline/
├── Main.java
├── model/
│   ├── Person.java
│   ├── Employee.java
│   ├── AirplanePilot.java
│   ├── StaffCabin.java
│   ├── Passenger.java
│   ├── Aircraft.java
│   ├── Airport.java
│   ├── Flight.java
│   ├── Booking.java
│   ├── FlightStatus.java
│   └── BookingStatus.java
├── service/
│   ├── AirlineSystem.java
│   └── StatisticsService.java
└── app/
    ├── ConsoleApp.java
    └── SampleData.java
```

## Requirements

- Java 17 or higher
- Apache Maven 3.6 or higher

## Build and run

Compile the project:

```
mvn clean compile
```

Run the application:

```
mvn exec:java
```

Or build an executable jar and run it:

```
mvn clean package
java -jar target/airline-management-1.0.0.jar
```

The application starts with sample data already loaded (airports, aircraft,
employees, passengers and flights) so every feature can be tested immediately
through the interactive console menu.
