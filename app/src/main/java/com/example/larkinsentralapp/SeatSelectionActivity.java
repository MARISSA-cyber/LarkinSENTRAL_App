package com.example.larkinsentralapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class SeatSelectionActivity extends AppCompatActivity {

    // Price per seat in RM
    private static final double PRICE_PER_SEAT = 35.0;

    // Row labels
    private static final String[] ROW_LABELS = {"A", "B", "C", "D", "E"};

    // Some seats pre-booked so the bus looks realistic
    private static final String[] BOOKED_SEATS = {"2A", "3A", "1C", "4C", "2D"};

    // All seats in the bus (5 rows × 4 seats)
    private final Seat[][] seats = new Seat[5][4];

    // Views for each seat button
    private final Button[][] seatButtons = new Button[5][4];

    // Bottom bar views
    private TextView tvSelectedSeats;
    private TextView tvSeatCount;
    private TextView tvTotalPrice;

    // Row container ids (matching the include ids in the layout)
    // We'll find them by their position in the parent, easier in code.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        tvSelectedSeats = findViewById(R.id.tvSelectedSeats);
        tvSeatCount     = findViewById(R.id.tvSeatCount);
        tvTotalPrice    = findViewById(R.id.tvTotalPrice);
        Button btnProceed = findViewById(R.id.btnProceed);

        initSeats();
        buildSeatGrid();
        updateBottomBar();

        btnProceed.setOnClickListener(v -> proceedToSummary());
    }

    // ── Build data model ──────────────────────────────────────────────────
    private void initSeats() {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 4; col++) {
                String seatId = getSeatId(row, col);
                int state = isPreBooked(seatId)
                        ? Seat.STATE_BOOKED
                        : Seat.STATE_AVAILABLE;
                seats[row][col] = new Seat(seatId, state);
            }
        }
    }

    // Returns "1A", "2A", "1B" … pattern: col+1 + rowLetter
    private String getSeatId(int row, int col) {
        return (col + 1) + ROW_LABELS[row];
    }

    private boolean isPreBooked(String seatId) {
        for (String booked : BOOKED_SEATS) {
            if (booked.equals(seatId)) return true;
        }
        return false;
    }

    // ── Build the UI seat grid ────────────────────────────────────────────
    private void buildSeatGrid() {
        // The layout has 5 includes: rowA…rowE
        // We find each include root by id.
        int[] rowIds = {R.id.rowA, R.id.rowB, R.id.rowC, R.id.rowD, R.id.rowE};

        for (int row = 0; row < 5; row++) {
            LinearLayout rowView = findViewById(rowIds[row]);

            // Set row label text (the first child TextView)
            TextView tvLabel = rowView.findViewById(R.id.tvRowLabel);
            tvLabel.setText(ROW_LABELS[row]);

            // Get the four seat buttons
            int[] seatBtnIds = {R.id.seat1, R.id.seat2, R.id.seat3, R.id.seat4};

            for (int col = 0; col < 4; col++) {
                Button btn = rowView.findViewById(seatBtnIds[col]);
                seatButtons[row][col] = btn;

                Seat seat = seats[row][col];
                btn.setText(seat.getSeatId());
                applySeatStyle(btn, seat);

                if (!seat.isBooked()) {
                    final int r = row, c = col;
                    btn.setOnClickListener(v -> onSeatClicked(r, c));
                }
            }
        }
    }

    // ── Handle seat tap ───────────────────────────────────────────────────
    private void onSeatClicked(int row, int col) {
        Seat seat = seats[row][col];

        if (seat.isBooked()) return; // should never happen, just safe

        if (seat.isAvailable()) {
            seat.setState(Seat.STATE_SELECTED);
        } else if (seat.isSelected()) {
            seat.setState(Seat.STATE_AVAILABLE);
        }

        applySeatStyle(seatButtons[row][col], seat);
        updateBottomBar();
    }

    // ── Apply background/text color based on seat state ───────────────────
    private void applySeatStyle(Button btn, Seat seat) {
        switch (seat.getState()) {
            case Seat.STATE_SELECTED:
                btn.setBackgroundResource(R.drawable.seat_selector);
                btn.setSelected(true);
                btn.setTextColor(ContextCompat.getColor(this, R.color.vintage_brown_dark));
                break;
            case Seat.STATE_BOOKED:
                btn.setBackgroundResource(R.drawable.seat_booked_bg);
                btn.setSelected(false);
                btn.setTextColor(ContextCompat.getColor(this, R.color.white));
                btn.setEnabled(false);
                break;
            default: // AVAILABLE
                btn.setBackgroundResource(R.drawable.seat_selector);
                btn.setSelected(false);
                btn.setTextColor(ContextCompat.getColor(this, R.color.vintage_brown));
                break;
        }
    }

    // ── Refresh bottom summary bar ─────────────────────────────────────────
    private void updateBottomBar() {
        List<String> selected = getSelectedSeatIds();
        double total = selected.size() * PRICE_PER_SEAT;

        if (selected.isEmpty()) {
            tvSelectedSeats.setText("None");
        } else {
            tvSelectedSeats.setText(join(selected));
        }

        tvSeatCount.setText(String.valueOf(selected.size()));
        tvTotalPrice.setText(String.format("RM %.2f", total));
    }

    // ── Proceed button ─────────────────────────────────────────────────────
    private void proceedToSummary() {
        List<String> selected = getSelectedSeatIds();

        if (selected.isEmpty()) {
            Toast.makeText(this, "Please select at least one seat.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, BookingSummaryActivity.class);
        intent.putStringArrayListExtra("selectedSeats", new ArrayList<>(selected));
        intent.putExtra("totalPrice", selected.size() * PRICE_PER_SEAT);
        startActivity(intent);
    }

    // ── Helpers ────────────────────────────────────────────────────────────
    private List<String> getSelectedSeatIds() {
        List<String> list = new ArrayList<>();
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 4; col++) {
                if (seats[row][col].isSelected()) {
                    list.add(seats[row][col].getSeatId());
                }
            }
        }
        return list;
    }

    private String join(List<String> items) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) sb.append("  ·  ");
            sb.append(items.get(i));
        }
        return sb.toString();
    }
}
