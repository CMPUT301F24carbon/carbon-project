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

import com.example.carbon_project.Model.Event;
import com.example.carbon_project.Model.Facility;
import com.example.carbon_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminEventListFragment extends Fragment {
    private FirebaseFirestore db;
    private CollectionReference eventsRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_event_list, container, false);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_tab2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Object> data = new ArrayList<>();

        AdminRecyclerViewAdapter adapter = new AdminRecyclerViewAdapter(data, position -> {
            Event clickedEvent = (Event) data.get(position);
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete Event")
                    .setMessage("Are you sure you want to delete this event?")
                    .setPositiveButton("Confirm", (dialog, which) -> clickedEvent.deleteEvent())
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
        });
        recyclerView.setAdapter(adapter);

        eventsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (value != null) {
                    data.clear();
                    for (QueryDocumentSnapshot doc:value) {
                        data.add(new Event(doc.getData()));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        return view;
    }
}
