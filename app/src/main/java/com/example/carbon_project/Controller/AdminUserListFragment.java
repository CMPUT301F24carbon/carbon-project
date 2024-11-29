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

import com.example.carbon_project.R;

import java.util.ArrayList;
import java.util.List;

public class AdminUserListFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_user_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_tab1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Object> data = new ArrayList<>();
        data.add("Tab1 Item 1");
        data.add("Tab1 Item 2");
        data.add("Tab1 Item 3");

        AdminRecyclerViewAdapter adapter = new AdminRecyclerViewAdapter(data);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
