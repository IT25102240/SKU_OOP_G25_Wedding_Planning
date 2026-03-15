package Daham_example;

public class Booking {
    private int bookingId;
    private String customerName;
    private String date;
    private String venue;

    // CREATE
    public void createBooking(int id, String name, String date, String venue) {
        this.bookingId = id;
        this.customerName = name;
        this.date = date;
        this.venue = venue;
    }

    // READ
    public void viewBooking() {
        System.out.println("Booking ID: " + bookingId);
        System.out.println("Customer: " + customerName);
        System.out.println("Date: " + date);
        System.out.println("Venue: " + venue);
    }

    // UPDATE
    public void updateBooking(String newDate, String newVenue) {
        this.date = newDate;
        this.venue = newVenue;
    }

    // DELETE
    public void deleteBooking() {
        this.bookingId = 0;
        this.customerName = null;
        this.date = null;
        this.venue = null;
    }
}
