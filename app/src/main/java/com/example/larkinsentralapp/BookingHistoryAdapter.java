package com.example.larkinsentralapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.ViewHolder> {

    private List<BookingHistoryModel> bookingList;

    public BookingHistoryAdapter(List<BookingHistoryModel> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookingHistoryModel booking = bookingList.get(position);

        holder.tvOrderId.setText("Order: " + safeText(booking.getOrderId()));
        holder.tvRoute.setText(
                safeText(booking.getOrigin()) + " → " + safeText(booking.getDestination())
        );
        holder.tvPrice.setText("RM " + booking.getTotalPrice());

        if (booking.getSelectedSeats() != null && !booking.getSelectedSeats().isEmpty()) {
            holder.tvSeats.setText("Seats: " + booking.getSelectedSeats().toString());
        } else {
            holder.tvSeats.setText("Seats: -");
        }
    }

    @Override
    public int getItemCount() {
        return bookingList == null ? 0 : bookingList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvRoute, tvDateTime, tvPassenger, tvSeats, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvRoute = itemView.findViewById(R.id.tvRoute);
            tvSeats = itemView.findViewById(R.id.tvSeats);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }

    private String safeText(String text) {
        return text == null || text.isEmpty() ? "-" : text;
    }
}
