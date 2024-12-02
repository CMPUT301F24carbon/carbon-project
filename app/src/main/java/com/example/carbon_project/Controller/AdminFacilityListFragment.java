package com.example.carbon_project.Controller;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.carbon_project.Model.Facility;
import com.example.carbon_project.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminFacilityListFragment extends Fragment {
    private FirebaseFirestore db;
    private CollectionReference facilitiesRef;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_facility_list, container, false);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        facilitiesRef = db.collection("facilities");

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_tab3);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Object> data = new ArrayList<>();

        AdminRecyclerViewAdapter adapter = new AdminRecyclerViewAdapter(data, position -> {
            Facility clickedFacility = (Facility) data.get(position);
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete Facility")
                    .setMessage("Are you sure you want to delete this facility and all its associated events?")
                    .setPositiveButton("Confirm", (dialog, which) -> clickedFacility.deleteFacility())
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();

        });
        recyclerView.setAdapter(adapter);


        facilitiesRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (value != null) {
                    data.clear();
                    for (QueryDocumentSnapshot doc: value) {
                        String facilityId = doc.getId();
                        String name = doc.getString("name");
                        String location = doc.getString("location");
                        int capacity = doc.getLong("capacity").intValue();
                        String orgId = doc.getString("organizerId");
                        data.add(new Facility(facilityId, name, location, capacity, orgId));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        return view;
    }
}
