package com.example.carbon_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carbon_project.Facility;

import java.util.ArrayList;

public class FacilityAdapter extends RecyclerView.Adapter<FacilityAdapter.FacilityViewHolder> {

    private ArrayList<Facility> facilities;

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

    public static class FacilityViewHolder extends RecyclerView.ViewHolder {
        TextView facilityName;
        TextView facilityLocation;
        TextView facilityCapacity;
        TextView facilityDescription;

        public FacilityViewHolder(View itemView) {
            super(itemView);
            facilityName = itemView.findViewById(R.id.facilityName);
            facilityLocation = itemView.findViewById(R.id.facilityLocation);
            facilityCapacity = itemView.findViewById(R.id.facilityCapacity);
            facilityDescription = itemView.findViewById(R.id.facilityDescription);
        }
    }
}
