package com.airline.service;

import com.airline.model.Booking;
import com.airline.model.BookingStatus;
import com.airline.model.Flight;
import com.airline.model.FlightStatus;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatisticsService {

    private final AirlineSystem system;

    public StatisticsService(AirlineSystem system) {
        this.system = system;
    }

    public int getTotalFlights() {
        return system.getFlights().size();
    }

    public int getActiveFlights() {
        int count = 0;
        for (Flight flight : system.getFlights()) {
            if (flight.getStatus() != FlightStatus.CANCELLED) {
                count++;
            }
        }
        return count;
    }

    public int getTotalPassengersCarried() {
        int total = 0;
        for (Flight flight : system.getFlights()) {
            total += flight.listingPassengers().size();
        }
        return total;
    }

    public double getTotalRevenue() {
        double revenue = 0;
        for (Booking booking : system.getBookings()) {
            if (booking.getStatus() == BookingStatus.CONFIRMED) {
                revenue += booking.getPrice();
            }
        }
        return revenue;
    }

    public Map<String, Integer> getMostPopularDestinations() {
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (Booking booking : system.getBookings()) {
            if (booking.getStatus() == BookingStatus.CANCELLED) {
                continue;
            }
            String city = booking.getFlight().getDestination().getCity();
            counts.merge(city, 1, Integer::sum);
        }
        return counts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new));
    }

    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("===== AIRLINE STATISTICS REPORT =====\n");
        report.append("Total flights planned: ").append(getTotalFlights()).append("\n");
        report.append("Active flights: ").append(getActiveFlights()).append("\n");
        report.append("Total passengers carried: ").append(getTotalPassengersCarried()).append("\n");
        report.append("Total confirmed revenue: ").append(getTotalRevenue()).append("\n");
        report.append("Most popular destinations:\n");
        Map<String, Integer> destinations = getMostPopularDestinations();
        if (destinations.isEmpty()) {
            report.append("   No reservations recorded yet.\n");
        } else {
            int rank = 1;
            for (Map.Entry<String, Integer> entry : destinations.entrySet()) {
                report.append("   ").append(rank).append(". ")
                        .append(entry.getKey()).append(" - ")
                        .append(entry.getValue()).append(" reservation(s)\n");
                rank++;
            }
        }
        report.append("=====================================");
        return report.toString();
    }

    public List<Flight> getFlights() {
        return system.getFlights();
    }
}
