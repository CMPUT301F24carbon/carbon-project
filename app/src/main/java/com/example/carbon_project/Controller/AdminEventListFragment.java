package com.example.carbon_project.Controller;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carbon_project.Model.Event;
import com.example.carbon_project.Model.Facility;
import com.example.carbon_project.R;

import java.util.ArrayList;
import java.util.List;

public class AdminEventListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_event_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_tab2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Object> data = new ArrayList<>();
        data.add(new Event("event1id", "Event 1", "Desc1", "False_Org", 21, true, "April1", "April2", "URL", "QR", new Facility("FID1", "Test Facility 1", "Unknown", 500, "False_Org")));
        data.add(new Event("event2id", "Event 2", "Desc2", "False_Org", 21, true, "April1", "April2", "URL", "QR", new Facility("FID1", "Test Facility 1", "Unknown", 500, "False_Org")));
        data.add(new Event("event3id", "Event 3", "Desc3", "False_Org", 21, true, "April1", "April2", "URL", "QR", new Facility("FID1", "Test Facility 1", "Unknown", 500, "False_Org")));

        AdminRecyclerViewAdapter adapter = new AdminRecyclerViewAdapter(data);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
