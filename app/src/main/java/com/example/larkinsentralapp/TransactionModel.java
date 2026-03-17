package com.example.larkinsentralapp;

public class TransactionModel {
    private String route;
    private String date;
    private String seat;
    private String price;

    public TransactionModel() {
        // Required for Firebase
    }

    public TransactionModel(String route, String date, String seat, String price) {
        this.route = route;
        this.date = date;
        this.seat = seat;
        this.price = price;
    }

    public String getRoute() {
        return route;
    }

    public String getDate() {
        return date;
    }

    public String getSeat() {
        return seat;
    }

    public String getPrice() {
        return price;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}