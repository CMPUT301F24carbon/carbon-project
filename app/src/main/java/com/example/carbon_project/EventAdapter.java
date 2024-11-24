package com.example.carbon_project;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private ArrayList<Event> events;
    private OnEventClickListener onEventClickListener;

    public EventAdapter(ArrayList<Event> events, OnEventClickListener onEventClickListener) {
        this.events = events;
        this.onEventClickListener = onEventClickListener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        // Get the current event object
        Event currentEvent = events.get(position);

        // Populate the ViewHolder with event data
        holder.eventNameTextView.setText(currentEvent.getName());
        holder.eventLocationTextView.setText(currentEvent.getLocation());
        holder.eventCapacityTextView.setText("Capacity: " + currentEvent.getCapacity());
        holder.eventStartDateTextView.setText("Start Date: " + currentEvent.getStartDate());
        holder.eventEndDateTextView.setText("End Date: " + currentEvent.getEndDate());

        // Set the onClickListener for the event item
        holder.itemView.setOnClickListener(v -> onEventClickListener.onEventClick(currentEvent));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView eventNameTextView;
        TextView eventLocationTextView;
        TextView eventCapacityTextView;
        TextView eventStartDateTextView;
        TextView eventEndDateTextView;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventNameTextView = itemView.findViewById(R.id.textView_event_name);
            eventLocationTextView = itemView.findViewById(R.id.textView_facility_name);
            eventCapacityTextView = itemView.findViewById(R.id.textView_capacity);
            eventStartDateTextView = itemView.findViewById(R.id.textView_start_date);
            eventEndDateTextView = itemView.findViewById(R.id.textView_end_date);
        }
    }

    public interface OnEventClickListener {
        void onEventClick(Event event);
    }
}

