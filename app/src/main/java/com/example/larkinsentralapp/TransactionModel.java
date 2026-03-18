package com.example.larkinsentralapp;

public class TransactionModel {

    private final String route;
    private final String date;
    private final String seat;
    private final String price;

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

}