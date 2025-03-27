package dsa_Metro;


import java.util.*;

public class EventManager22 {
    private static final int MAX_EVENTS = 50;

    private static class Event {
        String name;
        String description;
        String location;
        String date;
        int maxSeats;
        int availableSeats;
        Map<Integer, String> attendees;
        Queue<String> waitingList;

        public Event() {
            this.name = "";
            this.description = "";
            this.location = "";
            this.date = "";
            this.maxSeats = 0;
            this.availableSeats = 0;
            this.attendees = new HashMap<>();
            this.waitingList = new LinkedList<>();
        }
    }

    private static void createEvent(List<Event> events) {
        Scanner scanner = new Scanner(System.in);
        Event event = new Event();

        System.out.print("Enter event name: ");
        event.name = scanner.nextLine();
        System.out.print("Enter event description: ");
        event.description = scanner.nextLine();
        System.out.print("Enter event location: ");
        event.location = scanner.nextLine();
        System.out.print("Enter event date (dd/mm/yyyy): ");
        event.date = validateDate(scanner.nextLine());

        boolean validSeats = false;
        while (!validSeats) {
            try {
                System.out.print("Enter maximum number of seats: ");
                event.maxSeats = scanner.nextInt();
                event.availableSeats = event.maxSeats;
                validSeats = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer for the maximum number of seats.");
                scanner.next(); // Consume invalid input
            }
        }

        events.add(event);
    }

    private static String validateDate(String inputDate) {
        try {
            String[] parts = inputDate.split("/");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);

            boolean validDate = true;

            if (month < 1 || month > 12 || day < 1 || day > 31) {
                validDate = false;
            } else if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) {
                validDate = false;
            } else if (month == 2) {
                if (day > 29 || (day > 28 && !isLeapYear(year))) {
                    validDate = false;
                }
            }

            if (!validDate) {
                throw new NumberFormatException();
            }

            return inputDate;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid date format. Please enter a valid date (dd/mm/yyyy): ");
            Scanner scanner = new Scanner(System.in);
            return validateDate(scanner.nextLine());
        }
    }

    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    private static void displayEvents(List<Event> events) {
        System.out.println("Available Events:");
        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            System.out.printf("%d. %s - %s (Date: %s, Location: %s, Max Seats: %d, Available Seats: %d)\n",
                    i + 1, event.name, event.description, event.date, event.location, event.maxSeats, event.availableSeats);
        }
    }

    private static void bookSeat(List<Event> events, int eventIndex, String[] seatNumbers) {
        Scanner scanner = new Scanner(System.in);

        if (eventIndex < 0 || eventIndex >= events.size()) {
            System.out.println("Invalid event index. Please enter a valid event index.");
            return;
        }

        Event event = events.get(eventIndex);

        // Display already booked seats
        System.out.println("Already Booked Seats:");
        for (Map.Entry<Integer, String> entry : event.attendees.entrySet()) {
            System.out.printf("%d ", entry.getKey());
        }
        System.out.println();

        List<Integer> bookedSeats = new ArrayList<>();
        List<Integer> invalidSeats = new ArrayList<>();

        // Check if any seat number is already booked or invalid
        for (String seatNumberStr : seatNumbers) {
            if (seatNumberStr.isEmpty()) {
                System.out.println("Invalid seat number. Please enter valid seat numbers.");
                return;
            }
            int seatNumber;
            try {
                seatNumber = Integer.parseInt(seatNumberStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter valid seat numbers.");
                return;
            }
            if (event.attendees.containsKey(seatNumber)) {
                System.out.printf("Seat %d is already booked. Please choose another seat.\n", seatNumber);
                invalidSeats.add(seatNumber);
            } else if (seatNumber < 1 || seatNumber > event.maxSeats) {
                System.out.printf("Seat %d is invalid. Please choose a valid seat number.\n", seatNumber);
                invalidSeats.add(seatNumber);
            } else {
                bookedSeats.add(seatNumber);
            }
        }

        // Remove invalid seats from booking list
        List<String> validSeatNumbers = new ArrayList<>();
        for (String seatNumber : seatNumbers) {
            if (!invalidSeats.contains(Integer.parseInt(seatNumber))) {
                validSeatNumbers.add(seatNumber);
            }
        }

        if (!invalidSeats.isEmpty()) {
            System.out.println("Please choose valid seats and try again.");
        } else if (event.availableSeats >= validSeatNumbers.size()) {
            // Book all valid seats
            for (String seatNumberStr : validSeatNumbers) {
                int seatNumber = Integer.parseInt(seatNumberStr);
                event.attendees.put(seatNumber, "Booked");
                event.availableSeats--;
                System.out.printf("Seat %d booked successfully!\n", seatNumber);
            }
        } else {
            System.out.println("Not enough available seats for booking.");
            event.waitingList.addAll(validSeatNumbers);
            System.out.println("Added to waiting list.");
        }
    }

    private static void cancelBooking(List<Event> events, int eventIndex, int seatNumber) {
        Event event = events.get(eventIndex);
        if (event.attendees.containsKey(seatNumber)) {
            event.attendees.remove(seatNumber);
            event.availableSeats++;
            System.out.printf("Booking for Seat %d cancelled successfully!\n", seatNumber);

            if (!event.waitingList.isEmpty()) {
                String nextUser = event.waitingList.poll();
                System.out.printf("Allocating seat to %s from waiting list.\n", nextUser);
                event.attendees.put(seatNumber, nextUser);
            }
        } else {
            System.out.println("Seat is not booked. No action taken.");
        }
    }

    private static void displayBookedSeats(List<Event> events, int eventIndex) {
        Event event = events.get(eventIndex);
        System.out.printf("Booked Seats for %s:\n", event.name);
        for (Map.Entry<Integer, String> entry : event.attendees.entrySet()) {
            System.out.printf("%d ", entry.getKey());
        }
        System.out.println();
    }

    private static void addToWaitingList(List<Event> events, int eventIndex, String userName) {
        Event event = events.get(eventIndex);
        if (event.waitingList.size() < event.maxSeats) {
            event.waitingList.add(userName);
            System.out.printf("%s added to the waiting list for %s event.\n", userName, event.name);
        } else {
            System.out.printf("Sorry, the waiting list for %s event is full.\n", event.name);
        }
    }

    private static void removeEvent(List<Event> events, int eventIndex) {
        if (eventIndex >= 0 && eventIndex < events.size()) {
            Event removedEvent = events.remove(eventIndex);
            System.out.printf("Event '%s' removed successfully.\n", removedEvent.name);
        } else {
            System.out.println("Invalid event index. No event removed.");
        }
    }

    private static void displayWaitingList(List<Event> events, int eventIndex) {
        Event event = events.get(eventIndex);
        System.out.printf("Waiting List for %s event:\n", event.name);
        for (String user : event.waitingList) {
            System.out.println(user);
        }
    }

    private static void modifyEventDetails(Event event) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Current Event Details:");
        System.out.println("1. Name: " + event.name);
        System.out.println("2. Description: " + event.description);
        System.out.println("3. Location: " + event.location);
        System.out.println("4. Date: " + event.date);
        System.out.println("5. Max Seats: " + event.maxSeats);
        System.out.println("6. Exit");

        int choice;
        do {
            System.out.print("Enter your choice to modify: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter new name: ");
                    event.name = scanner.nextLine();
                    break;
                case 2:
                    System.out.print("Enter new description: ");
                    event.description = scanner.nextLine();
                    break;
                case 3:
                    System.out.print("Enter new location: ");
                    event.location = scanner.nextLine();
                    break;
                case 4:
                    System.out.print("Enter new date (dd/mm/yyyy): ");
                    event.date = validateDate(scanner.nextLine());
                    break;
                case 5:
                    System.out.print("Enter new max seats: ");
                    event.maxSeats = scanner.nextInt();
                    event.availableSeats = event.maxSeats;
                    break;
                case 6:
                    System.out.println("Exiting modification...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        } while (choice != 6);
    }

    public static void main(String[] args) {
        List<Event> events = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n1. Create Event");
            System.out.println("2. Display Available Events");
            System.out.println("3. Book Seat");
            System.out.println("4. Cancel Booking");
            System.out.println("5. Display Booked Seats");
            System.out.println("6. Add to Waiting List");
            System.out.println("7. Remove Event");
            System.out.println("8. Display Waiting List");
            System.out.println("9. Modify Event Details");
            System.out.println("10. Exit");
            System.out.print("Enter your choice: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    createEvent(events);
                    break;
                case 2:
                    displayEvents(events);
                    break;
                case 3:
                    System.out.print("Enter event index (1-" + events.size() + "): ");
                    int eventIndex;
                    try {
                        eventIndex = scanner.nextInt() - 1;
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a valid event index.");
                        break;
                    }
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter seat number(s) separated by comma (e.g., 1,2,3): ");
                    String seatNumbersInput = scanner.nextLine();
                    String[] seatNumbers = seatNumbersInput.split(",");
                    bookSeat(events, eventIndex, seatNumbers);
                    break;
                case 4:
                    System.out.print("Enter event index (1-" + events.size() + "): ");
                    eventIndex = scanner.nextInt() - 1;
                    System.out.print("Enter seat number to cancel booking: ");
                    int seatNumber = scanner.nextInt();
                    cancelBooking(events, eventIndex, seatNumber);
                    break;
                case 5:
                    System.out.print("Enter event index (1-" + events.size() + "): ");
                    eventIndex = scanner.nextInt() - 1;
                    displayBookedSeats(events, eventIndex);
                    break;
                case 6:
                    System.out.print("Enter event index (1-" + events.size() + "): ");
                    eventIndex = scanner.nextInt() - 1;
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter your name: ");
                    String userName = scanner.nextLine();
                    addToWaitingList(events, eventIndex, userName);
                    break;
                case 7:
                    System.out.print("Enter event index to remove (1-" + events.size() + "): ");
                    eventIndex = scanner.nextInt() - 1;
                    removeEvent(events, eventIndex);
                    break;
                case 8:
                    System.out.print("Enter event index (1-" + events.size() + "): ");
                    eventIndex = scanner.nextInt() - 1;
                    displayWaitingList(events, eventIndex);
                    break;
                case 9:
                    System.out.print("Enter event index to modify (1-" + events.size() + "): ");
                    eventIndex = scanner.nextInt() - 1;
                    scanner.nextLine(); // Consume newline
                    Event event = events.get(eventIndex);
                    modifyEventDetails(event);
                    break;
                case 10:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 10);
        scanner.close();
    }
}