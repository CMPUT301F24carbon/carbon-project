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

public class AdminFacilityListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_facility_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_tab3);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Object> data = new ArrayList<>();
        data.add(new Facility("Facility 1", "Anderossa", 150, "Test Facility"));
        data.add(new Facility("Facility 2", "Anderossa", 150, "Test Facility"));
        data.add(new Facility("Facility 3", "Anderossa", 150, "Test Facility"));

        AdminRecyclerViewAdapter adapter = new AdminRecyclerViewAdapter(data);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
