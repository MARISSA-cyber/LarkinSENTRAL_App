package com.example.larkinsentralapp;

// Simple model for one seat
public class Seat {

    public static final int STATE_AVAILABLE = 0;
    public static final int STATE_SELECTED  = 1;
    public static final int STATE_BOOKED    = 2;

    private String seatId;   // e.g. "1A", "3C"
    private int    state;

    public Seat(String seatId, int state) {
        this.seatId = seatId;
        this.state  = state;
    }

    public String getSeatId() { return seatId; }
    public int    getState()  { return state;  }
    public void   setState(int state) { this.state = state; }

    public boolean isAvailable() { return state == STATE_AVAILABLE; }
    public boolean isSelected()  { return state == STATE_SELECTED;  }
    public boolean isBooked()    { return state == STATE_BOOKED;    }
}
