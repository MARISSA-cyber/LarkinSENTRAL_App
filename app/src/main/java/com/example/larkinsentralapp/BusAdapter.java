package com.example.larkinsentralapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.BusViewHolder> {

    private Context context;
    private List<Bus> busList;
    private OnBusClickListener listener;

    public interface OnBusClickListener {
        void onBusSelected(Bus bus);
    }

    public BusAdapter(Context context, List<Bus> busList, OnBusClickListener listener) {
        this.context = context;
        this.busList = busList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bus, parent, false);
        return new BusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {
        Bus bus = busList.get(position);
        holder.name.setText(bus.getName());
        holder.fromTo.setText(bus.getFrom() + " → " + bus.getTo());
        holder.departure.setText("Departure: " + bus.getDepartureTime());
        holder.duration.setText("Duration: " + bus.getDuration());
        holder.price.setText("RM " + bus.getPrice());

        holder.selectButton.setOnClickListener(v -> listener.onBusSelected(bus));
    }

    @Override
    public int getItemCount() {
        return busList.size();
    }

    static class BusViewHolder extends RecyclerView.ViewHolder {
        TextView name, fromTo, departure, duration, price;
        Button selectButton;

        public BusViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.busName);
            fromTo = itemView.findViewById(R.id.busFromTo);
            departure = itemView.findViewById(R.id.busDeparture);
            duration = itemView.findViewById(R.id.busDuration);
            price = itemView.findViewById(R.id.busPrice);
            selectButton = itemView.findViewById(R.id.selectBusButton);
        }
    }
}