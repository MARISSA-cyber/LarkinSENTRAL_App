package com.example.larkinsentralapp;

public class Bus {
    private String name;
    private String from;
    private String to;
    private String departureTime;
    private String duration;
    private int price;
    private String time;

    public Bus() { } // Needed for Firebase

    public Bus(String name, String from, String to, String departureTime, String duration, int price,String time) {
        this.name = name;
        this.from = from;
        this.to = to;
        this.departureTime = departureTime;
        this.duration = duration;
        this.price = price;
        this.time = time;
    }

    // Getters
    public String getName() { return name; }
    public String getFrom() { return from; }
    public String getTo() { return to; }
    public String getTime() {
        return time;
    }
    public String getDepartureTime() { return departureTime; }
    public String getDuration() { return duration; }
    public int getPrice() { return price; }
}