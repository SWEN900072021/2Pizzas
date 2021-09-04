package com.twopizzas.domain.flight;

import com.twopizzas.domain.Airline;
import com.twopizzas.domain.Airport;
import com.twopizzas.domain.TimePeriod;
import com.twopizzas.util.AssertionConcern;

public class FlightSearch extends AssertionConcern {
    private final TimePeriod departing;
    private final TimePeriod returning;
    private final Airport from;
    private final Airport to;
    private final Airline airline;
    private final int passengers;

    public FlightSearchBuilder builder() {
        return new FlightSearchBuilder();
    }

    private FlightSearch(TimePeriod departing, TimePeriod returning, Airport from, Airport to, Airline airline, int passengers) {
        this.departing = departing;
        this.returning = returning;
        this.from = from;
        this.to = to;
        this.airline = airline;
        this.passengers = passengers;
    }

    public TimePeriod getDeparting() {
        return departing;
    }

    public TimePeriod getReturning() {
        return returning;
    }

    public Airport getFrom() {
        return from;
    }

    public Airport getTo() {
        return to;
    }

    public Airline getAirline() {
        return airline;
    }

    public int getPassengers() {
        return passengers;
    }

    public static class FlightSearchBuilder {
        private TimePeriod departing;
        private TimePeriod returning;
        private Airport from;
        private Airport to;
        private Airline airline;
        private int passengers;

        public FlightSearch build() {
            return new FlightSearch(departing, returning, from, to, airline, passengers);
        }

        public void setDeparting(TimePeriod departing) {
            this.departing = departing;
        }

        public void setReturning(TimePeriod returning) {
            this.returning = returning;
        }

        public void setFrom(Airport from) {
            this.from = from;
        }

        public void setTo(Airport to) {
            this.to = to;
        }

        public void setAirline(Airline airline) {
            this.airline = airline;
        }

        public void setPassengers(int passengers) {
            this.passengers = passengers;
        }
    }
}
