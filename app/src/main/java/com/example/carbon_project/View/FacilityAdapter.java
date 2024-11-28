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
public class FacilityAdapter extends RecyclerView.Adapter<FacilityAdapter.FacilityViewHolder> {

    private List<Facility> facilityList;
    private OnItemClickListener listener;

    public FacilityAdapter(List<Facility> facilityList) {
        this.facilityList = facilityList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FacilityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_facility, parent, false);
        return new FacilityViewHolder(itemView);
    }

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

    @Override
    public int getItemCount() {
        return facilityList.size();
    }

    public static class FacilityViewHolder extends RecyclerView.ViewHolder {
        TextView tvFacilityName, tvFacilityLocation, tvFacilityCapacity;

        public FacilityViewHolder(View itemView) {
            super(itemView);
            tvFacilityName = itemView.findViewById(R.id.tvFacilityName);
            tvFacilityLocation = itemView.findViewById(R.id.tvFacilityLocation);
            tvFacilityCapacity = itemView.findViewById(R.id.tvFacilityCapacity);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Facility facility, int position);
    }
}