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

    public EventAdapter(ArrayList<Event> events) {
        this.events = events;
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
        holder.eventNameTextView.setText(currentEvent.getName());
        holder.eventLocationTextView.setText(currentEvent.getLocation());
        holder.eventDateTextView.setText(currentEvent.getDate());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView eventNameTextView;
        TextView eventLocationTextView;
        TextView eventDateTextView;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventNameTextView = itemView.findViewById(R.id.textView_event_name);
            eventLocationTextView = itemView.findViewById(R.id.textView_facility_name);
            eventDateTextView = itemView.findViewById(R.id.textView_status);
        }
    }
}