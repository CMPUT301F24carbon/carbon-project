package com.example.carbon_project.Controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carbon_project.Model.Event;
import com.example.carbon_project.Model.Facility;
import com.example.carbon_project.Model.User;
import com.example.carbon_project.R;

import java.util.List;

/**
 * The AdminRecyclerViewAdapter class is an adapter for the RecyclerView in the AdminListActivity.
 */
public class AdminRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Object> data;
    private final OnItemClickListener listener;

    // View types
    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_EVENT = 2;
    private static final int VIEW_TYPE_FACILITY = 3;

    /**
     * The OnItemClickListener interface defines a callback method that is invoked
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    /**
     * Constructs a new AdminRecyclerViewAdapter object.
     * @param data
     * @param listener
     */
    public AdminRecyclerViewAdapter(List<Object> data, OnItemClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    /**
     * Returns the view type of the item at the given position.
     * @param position position to query
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        Object item = data.get(position);
        if (item instanceof User) {
            return VIEW_TYPE_USER;
        } else if (item instanceof Event) {
            return VIEW_TYPE_EVENT;
        } else if (item instanceof Facility) {
            return VIEW_TYPE_FACILITY;
        }
        throw new IllegalArgumentException("Unsupported data type at position " + position);
    }

    /**
     * Creates a new ViewHolder for the given view type.
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_USER) {
            View view = inflater.inflate(R.layout.user_item, parent, false);
            return new UserViewHolder(view, listener);
        } else if (viewType == VIEW_TYPE_EVENT) {
            View view = inflater.inflate(R.layout.event_item, parent, false);
            return new EventViewHolder(view, listener);
        } else if (viewType == VIEW_TYPE_FACILITY) {
            View view = inflater.inflate(R.layout.item_facility, parent, false);
            return new FacilityViewHolder(view, listener);
        }
        throw new IllegalArgumentException("Unsupported view type");
    }

    /**
     * Updates the contents of the ViewHolder to reflect the item at the given position.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = data.get(position);
        if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).bind((User) item);
        } else if (holder instanceof EventViewHolder) {
            ((EventViewHolder) holder).bind((Event) item);
        } else if (holder instanceof FacilityViewHolder) {
            ((FacilityViewHolder) holder).bind((Facility) item);
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     * @return
     */
    @Override
    public int getItemCount() {
        return data.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView email;
        TextView phone;


        /**
         * Constructs a new UserViewHolder object.
         * @param itemView
         * @param listener
         */
        public UserViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            phone = itemView.findViewById(R.id.phone);
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position);
                }
            });
        }

        /**
         * Binds the data to the view holder.
         * @param item
         */
        public void bind(User item) {
            name.setText(item.getName());
            email.setText(item.getEmail());
            if (item.getPhoneNumber() != null || !item.getPhoneNumber().isEmpty()) {
                phone.setVisibility(View.VISIBLE);
                phone.setText(item.getPhoneNumber());
            }
            else {
                phone.setVisibility(View.INVISIBLE);
            }
        }
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventNameTextView;
        TextView eventDescriptionView;
        TextView eventFacilityTextView;
        TextView eventCapacityTextView;
        TextView eventStartDateTextView;
        TextView eventEndDateTextView;

        /**
         * Constructs a new EventViewHolder object.
         * @param itemView
         * @param listener
         */
        public EventViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            eventNameTextView = itemView.findViewById(R.id.textView_event_name);
            eventDescriptionView = itemView.findViewById(R.id.textView_description);
            eventFacilityTextView = itemView.findViewById(R.id.textView_facility_name);
            eventCapacityTextView = itemView.findViewById(R.id.textView_capacity);
            eventStartDateTextView = itemView.findViewById(R.id.textView_start_date);
            eventEndDateTextView = itemView.findViewById(R.id.textView_end_date);
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position);
                }
            });
        }

        /**
         * Binds the data to the view holder.
         * @param item
         */
        public void bind(Event item) {
            eventNameTextView.setText(item.getName());
            eventDescriptionView.setText(item.getDescription());
            eventFacilityTextView.setText(item.getFacility().getName());
            eventCapacityTextView.setText(String.valueOf(item.getCapacity()));
            eventStartDateTextView.setText(item.getStartDate());
            eventEndDateTextView.setText(item.getEndDate());
        }
    }

    static class FacilityViewHolder extends RecyclerView.ViewHolder {
        TextView facilityName;
        TextView facilityLocation;
        TextView facilityCapacity;

        /**
         * Constructs a new FacilityViewHolder object.
         * @param itemView
         * @param listener
         */
        public FacilityViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            facilityName = itemView.findViewById(R.id.tvFacilityName);
            facilityLocation = itemView.findViewById(R.id.tvFacilityLocation);
            facilityCapacity = itemView.findViewById(R.id.tvFacilityCapacity);
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position);
                }
            });
        }

        /**
         * Binds the data to the view holder.
         * @param item
         */
        public void bind(Facility item) {
            facilityName.setText(item.getName());
            facilityLocation.setText(item.getLocation());
            facilityCapacity.setText(String.valueOf(item.getCapacity()));
        }
    }
}