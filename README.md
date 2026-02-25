# STROMJET Airline Reservation System

A Java console application that simulates a flight booking platform.

## Features
- **Booking**: Select routes between Delhi, Mumbai, Chennai, and Kolkata.
- **Dynamic Pricing**: First Class seats cost 2x the base fare.
- **Date Validation**: Restricts booking to a 3-month window using `java.time`.
- **Management**: View itinerary details or cancel tickets for a refund.

## Class Diagram
The project uses an Object-Oriented approach:
- **Route**: Handles flight paths and pricing.
- **Aircraft**: Manages fleet details.
- **Ticket**: Stores customer reservation data.



## How to Run
1. Compile: `javac AirlineTicketReservationSystem.java`
2. Run: `java AirlineTicketReservationSystem`
