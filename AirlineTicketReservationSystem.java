import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;

public class AirlineTicketReservationSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        LocalDate dt = LocalDate.now();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String presentDate = dt.format(df);
        LocalDate dtNext = dt.plusMonths(3);
        String nextDate = dtNext.format(df);

        int op = 1, ticketIndex = 0;
        String[] city = {"Delhi", "Mumbai", "Chennai", "Kolkata"};
        String[] airportType = {"Single", "Multihop"};
        
        Aircraft[] acft = new Aircraft[5];
        initializeAircrafts(acft);
        
        int totalRoutes = 20;
        Route[] flt = new Route[totalRoutes];
        initializeFlightRoutes(flt, totalRoutes, city);

        Ticket[] tck = new Ticket[10];
        for (int i = 0; i < 10; i++) {
            tck[i] = new Ticket();
            tck[i].ticketId = -1; 
        }

        System.out.println("\n---------------------------------------------------------------------");
        System.out.println("--------WELCOME TO STROMJET AIRLINE TICKET RESERVATION CENTER--------");
        System.out.println("---------------------------------------------------------------------");
        System.out.println("Book advance tickets between " + presentDate + " to " + nextDate + "\n");

        do {
            System.out.println("1.Flight Booking\t2.Cancellation\t\t3.Check Details");
            System.out.print("Enter choice: ");
            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1:
                    if (ticketIndex >= 10) {
                        System.out.println("System full (Max 10 tickets).");
                        break;
                    }
                    System.out.println("\n0.Delhi  1.Mumbai  2.Chennai  3.Kolkata");
                    System.out.print("Departure city: ");
                    int depr = sc.nextInt();
                    System.out.print("Arrival city: ");
                    int arvl = sc.nextInt();
                    sc.nextLine();

                    if (depr >= 0 && depr < 4 && arvl >= 0 && arvl < 4 && depr != arvl) {
                        String date = getTravelDate(sc, presentDate, nextDate, df);
                        System.out.println("\n-----------------Available Flights :------------------");
                        Set<Integer> validIdx = new HashSet<>();
                        for (int i = 0; i < totalRoutes; i++) {
                            if (flt[i].src.equals(city[depr]) && flt[i].dest.equals(city[arvl])) {
                                System.out.println("Press [" + i + "]");
                                flt[i].getroute();
                                acft[i % 5].getdetails();
                                validIdx.add(i);
                            }
                        }

                        if (validIdx.isEmpty()) {
                            System.out.println("No flights found for this route.");
                            break;
                        }

                        System.out.print("Choose Flight Index: ");
                        int choice = sc.nextInt();
                        if (!validIdx.contains(choice)) {
                            System.out.println("Invalid Selection!");
                            break;
                        }

                        int[] seatInfo = seatAvailability(sc);
                        System.out.print("\nConfirm Booking? (1=Yes, 0=No): ");
                        if (sc.nextInt() == 1) {
                            int finalFare = (seatInfo[1] == 1) ? flt[choice].fare * 2 : flt[choice].fare;
                            tck[ticketIndex].setticket(1001 + ticketIndex, date, (seatInfo[1] == 0 ? "Economy" : "First"), seatInfo[0], choice);
                            System.out.println("\nSuccess! Ticket ID: " + (1001 + ticketIndex));
                            System.out.println("Total Fare: ₹" + (finalFare * seatInfo[0]));
                            ticketIndex++;
                        }
                    } else {
                        System.out.println("Invalid city selection.");
                    }
                    break;

                case 2:
                    System.out.print("Enter Ticket ID: ");
                    int delId = sc.nextInt();
                    boolean foundDel = false;
                    for (int i = 0; i < 10; i++) {
                        if (tck[i].ticketId == delId) {
                            tck[i].ticketId = -1;
                            System.out.println("Ticket cancelled. Refund initiated.");
                            foundDel = true; break;
                        }
                    }
                    if (!foundDel) System.out.println("ID not found.");
                    break;

                case 3:
                    System.out.print("Enter Ticket ID: ");
                    int findId = sc.nextInt();
                    boolean foundView = false;
                    for (int i = 0; i < 10; i++) {
                        if (tck[i].ticketId == findId) {
                            int rIdx = tck[i].code;
                            System.out.println("\n--- RESERVATION DETAILS ---");
                            System.out.println("Runway Type: " + airportType[new Random().nextInt(2)]);
                            tck[i].getticket();
                            flt[rIdx].getroute();
                            acft[rIdx % 5].getdetails();
                            foundView = true; break;
                        }
                    }
                    if (!foundView) System.out.println("Ticket not found.");
                    break;
            }
            System.out.print("\nContinue? (1=Yes, 0=No): ");
            op = sc.nextInt();
        } while (op != 0);
        System.out.println("\nTHANK YOU FOR VISITING STROMJET!");
    }

    public static void initializeAircrafts(Aircraft[] acft) {
        for (int i = 0; i < 5; i++) acft[i] = new Aircraft();
        acft[0].setdetails("Airbus A320", "Passenger", 180, 730);
        acft[1].setdetails("Boeing 737", "Passenger", 132, 510);
        acft[2].setdetails("Airbus A330", "Passenger", 210, 650);
        acft[3].setdetails("Boeing 787", "Passenger", 240, 780);
        acft[4].setdetails("Airbus A350", "Passenger", 300, 900);
    }

    public static void initializeFlightRoutes(Route[] flt, int totalRoutes, String[] city){
        Random r = new Random();
        for (int i = 0; i < totalRoutes; i++) {
            int s = r.nextInt(city.length), d;
            do { d = r.nextInt(city.length); } while (s == d);
            flt[i] = new Route();
            flt[i].setroute("Moderate", 1000 + i, city[s], city[d], r.nextInt(24) + ":00", 3000 + r.nextInt(5000), 5);
        }
    }

    public static int[] seatAvailability(Scanner sc) {
        System.out.println("\nSeats Available: " + (10 + new Random().nextInt(40)));
        System.out.print("0.Economy  1.First Class: ");
        int type = sc.nextInt();
        System.out.print("Number of tickets: ");
        int num = sc.nextInt();
        return new int[]{num, type};
    }

    public static String getTravelDate(Scanner sc, String start, String end, DateTimeFormatter df) {
        sc.nextLine(); 
        while (true) {
            try {
                System.out.print("Enter date (" + start + " to " + end + "): ");
                String input = sc.nextLine();
                LocalDate date = LocalDate.parse(input, df);
                if (!date.isBefore(LocalDate.parse(start, df)) && !date.isAfter(LocalDate.parse(end, df))) return input;
            } catch (Exception e) { System.out.println("Invalid format! Use DD-MM-YYYY."); }
        }
    }
}

class Route {
    String traffic, src, dest, time;
    int code, fare, freq;
    void setroute(String st, int c, String s, String d, String t, int f, int fq) {
        traffic=st; code=c; src=s; dest=d; time=t; fare=f; freq=fq;
    }
    void getroute() { System.out.println("Flight " + code + ": " + src + " -> " + dest + " | Time: " + time + " | Fare: ₹" + fare); }
}

class Aircraft {
    String model, type; int cap, fuel;
    void setdetails(String m, String t, int c, int f) { model=m; type=t; cap=c; fuel=f; }
    void getdetails() { System.out.println("Aircraft: " + model + " | Capacity: " + cap); }
}

class Ticket {
    int ticketId, code, noSeats; String flightDate, seatType;
    void setticket(int id, String d, String s, int n, int c) { ticketId=id; flightDate=d; seatType=s; noSeats=n; code=c; }
    void getticket() { if (ticketId != -1) System.out.println("Ticket: " + ticketId + " | Date: " + flightDate + " | Class: " + seatType + " | Seats: " + noSeats); }
}