package com.example.carbon_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * FacilityAdapter is a RecyclerView.Adapter that binds Facility data to the views
 * in the facility_item layout. It is used to display a list of facilities in a RecyclerView.
 */
public class FacilityAdapter extends RecyclerView.Adapter<FacilityAdapter.FacilityViewHolder> {

    private ArrayList<Facility> facilities;

    /**
     * Constructs a new FacilityAdapter with the given list of facilities.
     * @param facilities The list of facilities to display.
     */
    public FacilityAdapter(ArrayList<Facility> facilities) {
        this.facilities = facilities;
    }

    @NonNull
    @Override
    public FacilityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for individual list items
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.facility_item, parent, false);
        return new FacilityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacilityViewHolder holder, int position) {
        Facility facility = facilities.get(position);
        holder.facilityName.setText(facility.getName());
        holder.facilityLocation.setText(facility.getLocation());
        holder.facilityCapacity.setText(String.valueOf(facility.getCapacity()));
        holder.facilityDescription.setText(facility.getDescription());
    }

    @Override
    public int getItemCount() {
        return facilities.size();
    }

    /**
     * FacilityViewHolder is a ViewHolder for a facility item view. It contains
     * references to the TextViews for facility details.
     */
    public static class FacilityViewHolder extends RecyclerView.ViewHolder {
        TextView facilityName;
        TextView facilityLocation;
        TextView facilityCapacity;
        TextView facilityDescription;

        /**
         * Constructs a new FacilityViewHolder and binds the views.
         * @param itemView The facility item view.
         */
        public FacilityViewHolder(View itemView) {
            super(itemView);
            facilityName = itemView.findViewById(R.id.facilityName);
            facilityLocation = itemView.findViewById(R.id.facilityLocation);
            facilityCapacity = itemView.findViewById(R.id.facilityCapacity);
            facilityDescription = itemView.findViewById(R.id.facilityDescription);
        }
    }
}