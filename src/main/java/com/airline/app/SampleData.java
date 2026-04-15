package com.airline.app;

import com.airline.model.AirplanePilot;
import com.airline.model.Aircraft;
import com.airline.model.Airport;
import com.airline.model.Flight;
import com.airline.model.Passenger;
import com.airline.model.StaffCabin;
import com.airline.service.AirlineSystem;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SampleData {

    public static void load(AirlineSystem system) {
        Airport cdg = new Airport("CDG", "Paris", "Paris Charles de Gaulle Airport");
        Airport jfk = new Airport("JFK", "New York", "John F. Kennedy International Airport");
        Airport lhr = new Airport("LHR", "London", "London Heathrow Airport");
        Airport dxb = new Airport("DXB", "Dubai", "Dubai International Airport");
        Airport nrt = new Airport("NRT", "Tokyo", "Narita International Airport");
        system.addAirport(cdg);
        system.addAirport(jfk);
        system.addAirport(lhr);
        system.addAirport(dxb);
        system.addAirport(nrt);

        Aircraft a1 = new Aircraft("F-GKXA", "Airbus A320", 180);
        Aircraft a2 = new Aircraft("F-HBNA", "Boeing 777", 320);
        Aircraft a3 = new Aircraft("F-GSPA", "Boeing 787", 250);
        system.addAircraft(a1);
        system.addAircraft(a2);
        system.addAircraft(a3);

        AirplanePilot p1 = new AirplanePilot("E1", "John Carter", "12 Rue de Paris", "john.carter@air.com",
                "EMP001", LocalDate.of(2015, 3, 12), "ATPL-4521", 8200);
        AirplanePilot p2 = new AirplanePilot("E2", "Sarah Lewis", "8 Baker Street", "sarah.lewis@air.com",
                "EMP002", LocalDate.of(2018, 7, 1), "ATPL-7788", 5400);
        StaffCabin c1 = new StaffCabin("E3", "Emma Brown", "5 Avenue Foch", "emma.brown@air.com",
                "EMP003", LocalDate.of(2019, 1, 20), "Safety and Service");
        StaffCabin c2 = new StaffCabin("E4", "Lucas Martin", "21 Rue Lafayette", "lucas.martin@air.com",
                "EMP004", LocalDate.of(2020, 9, 15), "First Class Service");
        StaffCabin c3 = new StaffCabin("E5", "Olivia Garcia", "3 Place Bellecour", "olivia.garcia@air.com",
                "EMP005", LocalDate.of(2021, 5, 10), "Multilingual Service");
        system.addEmployee(p1);
        system.addEmployee(p2);
        system.addEmployee(c1);
        system.addEmployee(c2);
        system.addEmployee(c3);

        Passenger pa1 = new Passenger("P1", "Alice Dupont", "10 Rue Victor Hugo", "alice@mail.com", "PA123456");
        Passenger pa2 = new Passenger("P2", "Bob Durand", "22 Rue Nationale", "bob@mail.com", "PB654321");
        Passenger pa3 = new Passenger("P3", "Chloe Petit", "7 Rue du Port", "chloe@mail.com", "PC987654");
        Passenger pa4 = new Passenger("P4", "David Moreau", "14 Rue de la Gare", "david@mail.com", "PD456789");
        system.addPassenger(pa1);
        system.addPassenger(pa2);
        system.addPassenger(pa3);
        system.addPassenger(pa4);

        Flight f1 = new Flight("AF100", cdg, jfk,
                LocalDateTime.of(2025, 1, 10, 8, 0),
                LocalDateTime.of(2025, 1, 10, 16, 30), 650.0);
        Flight f2 = new Flight("AF200", cdg, lhr,
                LocalDateTime.of(2025, 1, 10, 9, 30),
                LocalDateTime.of(2025, 1, 10, 10, 45), 180.0);
        Flight f3 = new Flight("AF300", cdg, dxb,
                LocalDateTime.of(2025, 1, 11, 22, 0),
                LocalDateTime.of(2025, 1, 12, 7, 30), 720.0);
        Flight f4 = new Flight("AF400", cdg, nrt,
                LocalDateTime.of(2025, 1, 12, 13, 0),
                LocalDateTime.of(2025, 1, 13, 8, 0), 980.0);
        system.addFlight(f1);
        system.addFlight(f2);
        system.addFlight(f3);
        system.addFlight(f4);

        system.affectFlight("AF100", "F-HBNA");
        system.affectFlight("AF200", "F-GKXA");
        system.affectFlight("AF300", "F-GSPA");

        system.assignFlight("AF100", "E1", java.util.List.of("E3", "E4"));
        system.assignFlight("AF200", "E2", java.util.List.of("E5"));

        system.confirmBook(system.bookFlight("P1", "AF100").getReservationNumber());
        system.confirmBook(system.bookFlight("P2", "AF100").getReservationNumber());
        system.confirmBook(system.bookFlight("P3", "AF200").getReservationNumber());
        system.bookFlight("P4", "AF100");
        system.confirmBook(system.bookFlight("P1", "AF200").getReservationNumber());
    }
}
