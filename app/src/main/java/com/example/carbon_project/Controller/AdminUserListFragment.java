package com.example.carbon_project.Controller;

import android.content.Intent;
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

import com.example.carbon_project.Model.Entrant;
import com.example.carbon_project.Model.Facility;
import com.example.carbon_project.Model.User;
import com.example.carbon_project.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The AdminUserListFragment class is a fragment that displays a list of users for an admin user.
 */
public class AdminUserListFragment extends Fragment{
    private FirebaseFirestore db;
    private CollectionReference usersRef;

    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_user_list, container, false);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("users");

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_tab1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Object> data = new ArrayList<>();
//        data.add("Tab1 Item 1");
//        data.add("Tab1 Item 2");
//        data.add("Tab1 Item 3");

        AdminRecyclerViewAdapter adapter = new AdminRecyclerViewAdapter(data, position -> {
            User clickedUser = (User) data.get(position);
            Intent intent = new Intent(requireContext(), AdminViewProfileActivity.class);
            intent.putExtra("userObject", clickedUser);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        usersRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (value != null) {
                    data.clear();
                    for (QueryDocumentSnapshot doc: value) {
                        if (!Objects.equals(doc.getString("role"), "admin")) {
                            String userId = doc.getId();
                            String name = doc.getString("name");
                            String email = doc.getString("email");
                            String phoneNumber = doc.getString("phoneNumber");
                            String role = doc.getString("role");
                            String pfpURL = doc.getString("profilePictureUrl");
                            data.add(new User(userId, name, email, phoneNumber, role, pfpURL));
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        return view;
    }
}
