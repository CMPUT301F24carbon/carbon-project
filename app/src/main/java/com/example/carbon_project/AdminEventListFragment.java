package com.example.carbon_project;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        data.add(new Event("Event 1", "123 Apple Way", 23, false, "April 1", "April 4", "Test event", 30, "Test URL"));
        data.add(new Event("Event 2", "123 Apple Way", 23, false, "April 1", "April 4", "Test event", 30, "Test URL"));
        data.add(new Event("Event 3", "123 Apple Way", 23, false, "April 1", "April 4", "Test event", 30, "Test URL"));

        AdminRecyclerViewAdapter adapter = new AdminRecyclerViewAdapter(data);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
