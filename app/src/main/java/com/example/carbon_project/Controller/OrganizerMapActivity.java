package com.example.carbon_project.Controller;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carbon_project.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

/**
 * The OrganizerMapActivity class is an activity that displays the map of an organizer.
 */
public class OrganizerMapActivity extends AppCompatActivity {

    private MapView mapView;

    /**
     * Called when the activity is starting.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configure OSMDroid
        Configuration.getInstance().setUserAgentValue(getPackageName());

        setContentView(R.layout.activity_organizer_map);

        // Initialize MapView
        mapView = findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK); // Set tile source
        mapView.setMultiTouchControls(true); // Enable pinch-to-zoom

        // Set initial map view
        IMapController mapController = mapView.getController();
        mapController.setZoom(10.0);
        GeoPoint startPoint = new GeoPoint(37.7749, -122.4194); // Example: San Francisco
        mapController.setCenter(startPoint);

        // Add a marker
        Marker marker = new Marker(mapView);
        marker.setPosition(startPoint);
        marker.setTitle("Marker in San Francisco");
        mapView.getOverlays().add(marker);
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
