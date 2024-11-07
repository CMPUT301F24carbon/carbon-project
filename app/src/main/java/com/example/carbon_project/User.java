package com.example.carbon_project;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Represents a user which is the superclass to Entrant, Admin and Organizer.
 */
public abstract class User {
    protected String userId;
    protected String name;
    protected String email;
    protected String phoneNumber;
    protected String profileImage;

    /**
     * Constructs a User object and writes it to firestore
     * @param userId      Unique identifier for the user.
     * @param name        Name of the user.
     * @param email       Email address of the user.
     * @param phoneNumber Phone number of the user.
     * @param profileImage  ProfileImage of the user.
     */
    public User(String userId, String name, String email, String phoneNumber, String profileImage) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profileImage = profileImage;

        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("phoneNumber", phoneNumber);
        
        FirebaseFirestore.getInstance().collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        return;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Set", "set failed with ", e);
                    }
                });
    }

    /**
     * Constructs a User object with Firebase info
     * @param id Unique id for the user
     * @throws NoSuchElementException Throws this when the user isn't in the database
     */
    public User(String id) throws NoSuchElementException {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        userId = id;
                        name = Objects.requireNonNull(document.get("name")).toString();
                        email = Objects.requireNonNull(document.get("email")).toString();
                        phoneNumber = Objects.requireNonNull(document.get("phoneNumber")).toString();

                    } else {
                        throw new NoSuchElementException("User does not exist");
                    }
                } else {
                    Log.d("Get","get failed with ", task.getException());
                }
            }
        });
    }

    public String getUserId() { return userId; }

    public String getName() { return name;}

    public String getEmail() { return email; }

    public String getPhoneNumber() { return phoneNumber; }

    public String getProfileImageUrl() { return profileImage; }

    public void setProfileImageUrl(String profileImage) {
        this.profileImage = profileImage;
    }

    public abstract void handleNotification(String message);
}