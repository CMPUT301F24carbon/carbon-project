package com.example.carbon_project.Controller;

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

        // Initialize Firestore and FirebaseAuth
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_tab2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Object> data = new ArrayList<>();


//        data.add(new Event("event1id", "Event 1", "Desc1", "False_Org", 21, true, "April1", "April2", "URL", "QR", new Facility("FID1", "Test Facility 1", "Unknown", 500, "False_Org")));
//        data.add(new Event("event2id", "Event 2", "Desc2", "False_Org", 21, true, "April1", "April2", "URL", "QR", new Facility("FID1", "Test Facility 1", "Unknown", 500, "False_Org")));
//        data.add(new Event("event3id", "Event 3", "Desc3", "False_Org", 21, true, "April1", "April2", "URL", "QR", new Facility("FID1", "Test Facility 1", "Unknown", 500, "False_Org")));

        AdminRecyclerViewAdapter adapter = new AdminRecyclerViewAdapter(data);
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
                        // collect data needed for event class
                        String eventId = doc.getId();
                        String eventName = doc.getString("name");
                        String description = doc.getString("description");
                        String orgID = doc.getString("organizerId");
                        int capacity = doc.getLong("capacity").intValue();
                        boolean geolocationRequired = doc.getBoolean("geolocationRequired");
                        String startDate = doc.getString("startDate");
                        String endDate = doc.getString("endDate");
                        String eventPosterURL = doc.getString("eventPosterURL");
                        String qrURL = doc.getString("qrCodeURL");
                        // collect data from facility Map needed for facility object that goes in event object
                        String facilityID = ((HashMap<String, Object>) doc.getData().get("facility")).get("facilityId").toString();
                        String facilityName = ((HashMap<String, Object>) doc.getData().get("facility")).get("name").toString();
                        String facilityLocation = ((HashMap<String, Object>) doc.getData().get("facility")).get("location").toString();
                        int facilityCapacity = Integer.parseInt(((HashMap<String, Object>) doc.getData().get("facility")).get("capacity").toString());
                        String facilityOrganizer = ((HashMap<String, Object>) doc.getData().get("facility")).get("organizerId").toString();
                        data.add(new Event(eventId, eventName, description, orgID, capacity, geolocationRequired, startDate, endDate, eventPosterURL ,qrURL, new Facility(facilityID, facilityName, facilityLocation, facilityCapacity, facilityOrganizer)));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        return view;
    }
}
