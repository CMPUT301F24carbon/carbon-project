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

import com.example.carbon_project.Model.Facility;
import com.example.carbon_project.R;

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
        data.add(new Facility("facilityID1", "Facility 1", "Anderossa", 500, "False_Org"));
        data.add(new Facility("facilityID2", "Facility 2", "Anderossa", 500, "Falser_Org"));
        data.add(new Facility("facilityID3", "Facility 3", "Anderossa", 500, "Falsest_Org"));

        AdminRecyclerViewAdapter adapter = new AdminRecyclerViewAdapter(data);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
