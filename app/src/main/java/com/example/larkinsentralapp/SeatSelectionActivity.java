package com.example.larkinsentralapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class SeatSelectionActivity extends AppCompatActivity {

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

    // Price per seat
    private double PRICE_PER_SEAT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        // Get intent data
        Intent intent = getIntent();
        String busName = intent.getStringExtra("busName");
        double price = intent.getDoubleExtra("price", 0.0);
        String origin = intent.getStringExtra("origin");
        String destination = intent.getStringExtra("destination");
        String departDate = intent.getStringExtra("departDate");
        String time = intent.getStringExtra("time");

        PRICE_PER_SEAT = price;

        // Bottom bar views
        tvSelectedSeats = findViewById(R.id.tvSelectedSeats);
        tvSeatCount = findViewById(R.id.tvSeatCount);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);

        // Bus info header
        TextView tvBusName = findViewById(R.id.tvBusName);
        TextView tvPrice = findViewById(R.id.tvPrice);
        tvBusName.setText(busName);
        tvPrice.setText(String.format("RM %.2f / seat", price));

        // Trip info
        TextView tvFrom = findViewById(R.id.tvFrom);
        TextView tvTo = findViewById(R.id.tvTo);
        TextView tvTime = findViewById(R.id.tvTime);
        TextView tvDate = findViewById(R.id.tvDate);
        tvFrom.setText(origin);
        tvTo.setText(destination);
        tvTime.setText(time);
        tvDate.setText(departDate);

        // Initialize seats
        initSeats();
        buildSeatGrid();
        updateBottomBar();

        // Proceed button
        View btnProceed = findViewById(R.id.btnProceed);
        btnProceed.setOnClickListener(v -> proceedToSummary());
    }

    // ── Build data model ──────────────────────────────────────────────────
    private void initSeats() {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 4; col++) {
                String seatId = getSeatId(row, col);
                int state = isPreBooked(seatId) ? Seat.STATE_BOOKED : Seat.STATE_AVAILABLE;
                seats[row][col] = new Seat(seatId, state);
            }
        }
    }

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
        int[] rowIds = {R.id.rowA, R.id.rowB, R.id.rowC, R.id.rowD, R.id.rowE};

        for (int row = 0; row < 5; row++) {
            LinearLayout rowView = findViewById(rowIds[row]);

            // Set row label
            TextView tvLabel = rowView.findViewById(R.id.tvRowLabel);
            tvLabel.setText(ROW_LABELS[row]);

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

        if (seat.isBooked()) return;

        if (seat.isAvailable()) seat.setState(Seat.STATE_SELECTED);
        else if (seat.isSelected()) seat.setState(Seat.STATE_AVAILABLE);

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
            default:
                btn.setBackgroundResource(R.drawable.seat_selector);
                btn.setSelected(false);
                btn.setTextColor(ContextCompat.getColor(this, R.color.vintage_brown));
        }
    }

    // ── Refresh bottom summary bar ─────────────────────────────────────────
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void updateBottomBar() {
        List<String> selected = getSelectedSeatIds();
        double total = selected.size() * PRICE_PER_SEAT;

        tvSelectedSeats.setText(selected.isEmpty() ? "None" : join(selected));
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

        Intent intent = new Intent(SeatSelectionActivity.this, BookingSummaryActivity.class);

        // Pass selected seats
        intent.putStringArrayListExtra("selectedSeats", new ArrayList<>(selected));

        // Pass total price
        double totalPrice = selected.size() * PRICE_PER_SEAT;
        intent.putExtra("totalPrice", totalPrice);

        // Pass bus info
        intent.putExtra("busName", getIntent().getStringExtra("busName"));
        intent.putExtra("pricePerSeat", PRICE_PER_SEAT);
        intent.putExtra("from", getIntent().getStringExtra("from"));
        intent.putExtra("to", getIntent().getStringExtra("to"));
        intent.putExtra("time", getIntent().getStringExtra("time"));

        // Pass trip info
        intent.putExtra("origin", getIntent().getStringExtra("origin"));
        intent.putExtra("destination", getIntent().getStringExtra("destination"));
        intent.putExtra("departDate", getIntent().getStringExtra("departDate"));
        intent.putExtra("returnDate", getIntent().getStringExtra("returnDate"));
        intent.putExtra("isReturnTrip", getIntent().getBooleanExtra("isReturnTrip", false));

        startActivity(intent);
    }

    // ── Helpers ────────────────────────────────────────────────────────────
    private List<String> getSelectedSeatIds() {
        List<String> list = new ArrayList<>();
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 4; col++) {
                if (seats[row][col].isSelected()) list.add(seats[row][col].getSeatId());
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