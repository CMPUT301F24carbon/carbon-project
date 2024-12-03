package com.example.carbon_project.Model;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a notification in the system.
 */
public class Notification {

    /**
     * Sends notifications to a list of users.
     * @param users
     * @param body
     */
    public static void sendtoUsers(List<String> users, String body) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (users != null) {
            // Fetch the FCM tokens for these users
            db.collection("users")
                    .whereIn("userId", users)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        List<String> fcmTokens = new ArrayList<>();
                        for (QueryDocumentSnapshot userDoc : querySnapshot) {
                            String token = userDoc.getString("fcm");
                            if (token != null) {
                                fcmTokens.add(token);
                            }
                        }

                        // Send notifications using FCM
                        if (!fcmTokens.isEmpty()) {
                            sendFcmNotifications(fcmTokens, body);
                        }
                    });
        }
    }

    /**
     * Send a notification to all selected users in an event.
     * @param eventId
     * @param body
     */
    public static void sendToSelected(String eventId, String body) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events")
                .document(eventId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Get list of user IDs in the event
                        List<String> userIds = (List<String>) documentSnapshot.get("selectedList");

                        if (userIds != null) {
                            // Fetch the FCM tokens for these users
                            db.collection("users")
                                    .whereIn("userId", userIds)
                                    .get()
                                    .addOnSuccessListener(querySnapshot -> {
                                        List<String> fcmTokens = new ArrayList<>();
                                        for (QueryDocumentSnapshot userDoc : querySnapshot) {
                                            String token = userDoc.getString("fcm");
                                            if (token != null) {
                                                fcmTokens.add(token);
                                            }
                                        }

                                        // Send notifications using FCM
                                        if (!fcmTokens.isEmpty()) {
                                            sendFcmNotifications(fcmTokens, body);
                                        }
                                    });
                        }
                    }
                });
    }

    /**
     * Send a notification to all waiting users in an event.
     * @param eventId
     * @param body
     */
    public static void sendToWating(String eventId, String body) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events")
                .document(eventId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Get list of user IDs in the event
                        List<String> userIds = (List<String>) documentSnapshot.get("waitingList");

                        if (userIds != null && !userIds.isEmpty()) {
                            // Fetch the FCM tokens for these users
                            db.collection("users")
                                    .whereIn("userId", userIds)
                                    .get()
                                    .addOnSuccessListener(querySnapshot -> {
                                        List<String> fcmTokens = new ArrayList<>();
                                        for (QueryDocumentSnapshot userDoc : querySnapshot) {
                                            String token = userDoc.getString("fcm");
                                            if (token != null) {
                                                fcmTokens.add(token);
                                            }
                                        }

                                        // Send notifications using FCM
                                        if (!fcmTokens.isEmpty()) {
                                            sendFcmNotifications(fcmTokens, body);
                                        }
                                    });
                        }
                    }
                });
    }

    /**
     * Send a notification to all enrolled users in an event.
     * @param eventId
     * @param body
     */
    public static void sendToEnrolled(String eventId, String body) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events")
                .document(eventId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Get list of user IDs in the event
                        List<String> userIds = (List<String>) documentSnapshot.get("enrolledList");

                        if (userIds != null) {
                            // Fetch the FCM tokens for these users
                            db.collection("users")
                                    .whereIn("userId", userIds)
                                    .get()
                                    .addOnSuccessListener(querySnapshot -> {
                                        List<String> fcmTokens = new ArrayList<>();
                                        for (QueryDocumentSnapshot userDoc : querySnapshot) {
                                            String token = userDoc.getString("fcm");
                                            if (token != null) {
                                                fcmTokens.add(token);
                                            }
                                        }

                                        // Send notifications using FCM
                                        if (!fcmTokens.isEmpty()) {
                                            sendFcmNotifications(fcmTokens, body);
                                        }
                                    });
                        }
                    }
                });
    }

    /**
     * Send FCM notifications to a list of tokens.
     * @param fcmTokens
     * @param body
     */
    private static void sendFcmNotifications(List<String> fcmTokens, String body) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> map = new HashMap<>();
        map.put("title", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        map.put("body", body);
        map.put("fcmTokens", fcmTokens);
        db.collection("notifications")
                .add(map)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Document successfully updated!"))
                .addOnFailureListener(e -> Log.w("Firestore", "Error updating document", e));

    }

}