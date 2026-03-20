package com.example.larkinsentralapp;

import java.util.ArrayList;

public class BookingHistoryModel {
    private String orderId;
    private String origin;
    private String destination;
    private String departDate;

    private double totalPrice;
    private ArrayList<String> selectedSeats;

    public BookingHistoryModel() {
    }

    public BookingHistoryModel(String orderId, String origin, String destination,
                               String departDate, double totalPrice, ArrayList<String> selectedSeats) {
        this.orderId = orderId;
        this.origin = origin;
        this.destination = destination;
        this.departDate = departDate;
        this.totalPrice = totalPrice;
        this.selectedSeats = selectedSeats;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getDepartDate() {
        return departDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public ArrayList<String> getSelectedSeats() {
        return selectedSeats;
    }
}
