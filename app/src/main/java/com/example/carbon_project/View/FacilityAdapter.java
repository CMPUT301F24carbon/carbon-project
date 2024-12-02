package com.example.carbon_project.View;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carbon_project.Model.Facility;
import com.example.carbon_project.R;

import java.util.List;

/**
 * The FacilityAdapter class is a custom adapter for displaying a list of facilities in a RecyclerView.
 */
public class FacilityAdapter extends RecyclerView.Adapter<FacilityAdapter.FacilityViewHolder> {

    private List<Facility> facilityList;
    private OnItemClickListener listener;

    /**
     * Constructs a new FacilityAdapter with the given list of facilities.
     * @param facilityList
     */
    public FacilityAdapter(List<Facility> facilityList) {
        this.facilityList = facilityList;
    }

    /**
     * Sets the click listener for the adapter.
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return
     */
    @NonNull
    @Override
    public FacilityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_facility, parent, false);
        return new FacilityViewHolder(itemView);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull FacilityViewHolder holder, int position) {
        Facility facility = facilityList.get(position);
        holder.tvFacilityName.setText(facility.getName());
        holder.tvFacilityLocation.setText(facility.getLocation());
        holder.tvFacilityCapacity.setText(String.valueOf(facility.getCapacity()));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(facility, position);
            }
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     * @return
     */
    @Override
    public int getItemCount() {
        return facilityList.size();
    }

    /**
     * The FacilityViewHolder class represents a single item view in the RecyclerView.
     */
    public static class FacilityViewHolder extends RecyclerView.ViewHolder {
        TextView tvFacilityName, tvFacilityLocation, tvFacilityCapacity;

        /**
         * Constructs a new FacilityViewHolder with the given view.
         * @param itemView
         */
        public FacilityViewHolder(View itemView) {
            super(itemView);
            tvFacilityName = itemView.findViewById(R.id.tvFacilityName);
            tvFacilityLocation = itemView.findViewById(R.id.tvFacilityLocation);
            tvFacilityCapacity = itemView.findViewById(R.id.tvFacilityCapacity);
        }
    }

    /**
     * The OnItemClickListener interface defines a callback method for handling item clicks.
     */
    public interface OnItemClickListener {
        void onItemClick(Facility facility, int position);
    }
}