package com.example.carbon_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AdminRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Object> data;

    // View types
    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_EVENT = 2;
    private static final int VIEW_TYPE_FACILITY = 3;

    public AdminRecyclerViewAdapter(List<Object> data) {
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = data.get(position);
        if (item instanceof String) {
            return VIEW_TYPE_USER;
        } else if (item instanceof Event) {
            return VIEW_TYPE_EVENT;
        } else if (item instanceof Facility) {
            return VIEW_TYPE_FACILITY;
        }
        throw new IllegalArgumentException("Unsupported data type at position " + position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_USER) {
            View view = inflater.inflate(R.layout.user_item, parent, false);
            return new UserViewHolder(view);
        } else if (viewType == VIEW_TYPE_EVENT) {
            View view = inflater.inflate(R.layout.event_list_item, parent, false);
            return new EventViewHolder(view);
        } else if (viewType == VIEW_TYPE_FACILITY) {
            View view = inflater.inflate(R.layout.facility_item, parent, false);
            return new FacilityViewHolder(view);
        }
        throw new IllegalArgumentException("Unsupported view type");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = data.get(position);
        if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).bind((String) item);
        } else if (holder instanceof EventViewHolder) {
            ((EventViewHolder) holder).bind((Event) item);
        } else if (holder instanceof FacilityViewHolder) {
            ((FacilityViewHolder) holder).bind((Facility) item);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_text);
        }

        public void bind(String item) {
            textView.setText(item);
        }
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
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

        public void bind(Event item) {
            eventNameTextView.setText(item.getName());
            eventLocationTextView.setText(item.getLocation());
            eventCapacityTextView.setText(item.getCapacity());
            eventStartDateTextView.setText(item.getStartDate());
            eventEndDateTextView.setText(item.getEndDate());
        }
    }

    static class FacilityViewHolder extends RecyclerView.ViewHolder {
        TextView facilityName;
        TextView facilityLocation;
        TextView facilityCapacity;
        TextView facilityDescription;

        public FacilityViewHolder(@NonNull View itemView) {
            super(itemView);
            facilityName = itemView.findViewById(R.id.facilityName);
            facilityLocation = itemView.findViewById(R.id.facilityLocation);
            facilityCapacity = itemView.findViewById(R.id.facilityCapacity);
            facilityDescription = itemView.findViewById(R.id.facilityDescription);
        }

        public void bind(Facility item) {
            facilityName.setText(item.getName());
            facilityLocation.setText(item.getLocation());
            facilityCapacity.setText(item.getCapacity());
            facilityDescription.setText(item.getDescription());
        }
    }
}