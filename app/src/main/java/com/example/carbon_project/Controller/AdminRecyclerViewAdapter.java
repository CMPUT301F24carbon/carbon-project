package com.example.carbon_project.Controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carbon_project.Model.Event;
import com.example.carbon_project.Model.Facility;
import com.example.carbon_project.Model.User;
import com.example.carbon_project.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Object> data;
    private final OnItemClickListener listener;

    // View types
    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_EVENT = 2;
    private static final int VIEW_TYPE_FACILITY = 3;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public AdminRecyclerViewAdapter(List<Object> data, OnItemClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

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

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePicture;
        TextView name;
        TextView email;
        TextView phone;


        public UserViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            profilePicture = itemView.findViewById(R.id.profile_picture);
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

        public void bind(User item) {
            if (item.getProfilePictureUrl() != null && !item.getProfilePictureUrl().isEmpty()) {
                Picasso.get()
                        .load(item.getProfilePictureUrl())
                        .into(profilePicture);
            }
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
        ImageView eventImage;
        TextView eventNameTextView;
        TextView eventDescriptionView;
        TextView eventFacilityTextView;
        TextView eventCapacityTextView;
        TextView eventStartDateTextView;
        TextView eventEndDateTextView;

        public EventViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            eventImage = itemView.findViewById(R.id.event_image);
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

        public void bind(Event item) {
            if (item.getEventPosterUrl() != null && !item.getEventPosterUrl().isEmpty()) {
                Picasso.get()
                        .load(item.getEventPosterUrl())
                        .into(eventImage);
            }
            eventNameTextView.setText(item.getName());
            eventDescriptionView.setText(item.getDescription());
            eventFacilityTextView.setText(item.getOrganizerId());
            eventCapacityTextView.setText(String.valueOf(item.getCapacity()));
            eventStartDateTextView.setText(item.getStartDate());
            eventEndDateTextView.setText(item.getEndDate());
        }
    }

    static class FacilityViewHolder extends RecyclerView.ViewHolder {
        TextView facilityName;
        TextView facilityLocation;
        TextView facilityCapacity;

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

        public void bind(Facility item) {
            facilityName.setText(item.getName());
            facilityLocation.setText(item.getLocation());
            facilityCapacity.setText(String.valueOf(item.getCapacity()));
        }
    }
}