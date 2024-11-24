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

    /**
     * Constructs a new EventAdapter with the given list of events.
     * @param events The list of events to display.
     * @param onEventClickListener Listener to handle event item clicks.
     */
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

        holder.eventNameTextView.setText(currentEvent.getName());
        holder.eventLocationTextView.setText(currentEvent.getLocation());
        holder.eventCapacityTextView.setText("Capacity: " + currentEvent.getCapacity());
        holder.eventStartDateTextView.setText("Start Date: " + currentEvent.getStartDate());
        holder.eventEndDateTextView.setText("End Date: " + currentEvent.getEndDate());
        //holder.eventDescriptionTextView.setText("Description: " + currentEvent.getDescription());

        // Set the onClickListener for the event item
        holder.itemView.setOnClickListener(v -> onEventClickListener.onEventClick(currentEvent));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    /**
     * EventViewHolder is a ViewHolder for an event item view. It contains
     * references to the TextViews for event details.
     */
    public static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView eventNameTextView;
        TextView eventLocationTextView;
        TextView eventStatusTextView;
        TextView eventCapacityTextView;
        TextView eventStartDateTextView;
        TextView eventEndDateTextView;
        TextView eventDescriptionTextView;

        /**
         * Constructs a new EventViewHolder and binds the views.
         * @param itemView The event item view.
         */
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventNameTextView = itemView.findViewById(R.id.textView_event_name);
            eventLocationTextView = itemView.findViewById(R.id.textView_facility_name);
            eventStatusTextView = itemView.findViewById(R.id.textView_status);
            eventCapacityTextView = itemView.findViewById(R.id.textView_capacity);
            eventStartDateTextView = itemView.findViewById(R.id.textView_start_date);
            eventEndDateTextView = itemView.findViewById(R.id.textView_end_date);
            //eventDescriptionTextView = itemView.findViewById(R.id.textView_description);
        }
    }

    // Define the interface for item click handling
    public interface OnEventClickListener {
        void onEventClick(Event event);
    }
}