package com.example.carbon_project.Controller;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carbon_project.R;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.Collections;

/**
 * The EntrantJoinEventActivity class is an activity that allows an entrant to join an event.
 */
public class EntrantJoinEventActivity extends AppCompatActivity {
    private DecoratedBarcodeView barcodeView;

    /**
     * Called when the activity is starting.
     * @param savedInstanceState
     */
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
        /**
         * Called when a barcode result is received.
         * @param result the result
         */
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

    /**
     * Handle the scanned QR code.
     * @param scannedData
     */
    private void handleScanResult(String scannedData) {
        Intent intent = new Intent(this, EventDetailsActivity.class);
        intent.putExtra("eventId", scannedData);
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
