package com.example.carbon_project.Controller;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carbon_project.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class OrganizerMapActivity extends AppCompatActivity {

    private MapView mapView;
    private FirebaseFirestore db;
    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configure OSMDroid
        Configuration.getInstance().setUserAgentValue(getPackageName());

        setContentView(R.layout.activity_organizer_map);

        eventId = getIntent().getStringExtra("eventId");

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize MapView
        mapView = findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK); // Set tile source
        mapView.setMultiTouchControls(true); // Enable pinch-to-zoom

        // Set initial map view
        IMapController mapController = mapView.getController();
        mapController.setZoom(10.0);
        GeoPoint startPoint = new GeoPoint(37.7749, -122.4194); // Example: San Francisco
        mapController.setCenter(startPoint);

        // Fetch geolocations from Firestore and add markers
        fetchGeolocationsAndAddMarkers();
    }

    private void fetchGeolocationsAndAddMarkers() {
        // Fetch the geolocation data (latitude and longitude) from Firestore
        db.collection("events") // Replace with the actual collection containing event data
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Double latitude = document.getDouble("latitude");
                        Double longitude = document.getDouble("longitude");

                        if (latitude != null && longitude != null) {
                            // Create a GeoPoint for the location
                            GeoPoint geoPoint = new GeoPoint(latitude, longitude);

                            // Add a marker on the map
                            Marker marker = new Marker(mapView);
                            marker.setPosition(geoPoint);
                            marker.setTitle(document.getString("eventName")); // Set event name as the marker title
                            mapView.getOverlays().add(marker);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure if needed
                    Toast.makeText(this, "Error fetching geolocations: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }
}

