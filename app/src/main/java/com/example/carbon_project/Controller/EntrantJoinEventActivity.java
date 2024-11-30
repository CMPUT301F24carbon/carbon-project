package com.example.carbon_project.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carbon_project.Model.Entrant;
import com.example.carbon_project.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.Collections;

public class EntrantJoinEventActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private Entrant entrant;

    private DecoratedBarcodeView barcodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrant_join_event);

        // Initialize the barcode scanner view
        barcodeView = findViewById(R.id.barcode_scanner);

        // Restrict scanner to QR codes only
        barcodeView.getBarcodeView().setDecoderFactory(
                new com.journeyapps.barcodescanner.DefaultDecoderFactory(
                        Collections.singletonList(BarcodeFormat.QR_CODE)
                )
        );

        // Start scanning
        barcodeView.decodeContinuous(callback);
    }

    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null) {
                String scannedData = result.getText();

                // Stop scanning after a result is received
                barcodeView.pause();

                // Handle the scanned QR code
                handleScanResult(scannedData);
            }
        }
    };

    private void handleScanResult(String scannedData) {
        db = FirebaseFirestore.getInstance();
        entrant.joinEvent(scannedData);
        db.collection("users").document(entrant.getUserId())
                .update("joinedEvents", entrant.getJoinedEvents())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Event joined successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to join event: " + e.getMessage(), Toast.LENGTH_SHORT).show());

        Intent intent = new Intent(this, EntrantDashboardActivity.class);
        intent.putExtra("userObject", entrant);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume(); // Resume scanning
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause(); // Pause scanning
    }
}
