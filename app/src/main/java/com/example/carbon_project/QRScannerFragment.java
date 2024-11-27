package com.example.carbon_project;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class QRScannerFragment extends DialogFragment {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;

    // Create an instance of the ScanContract
    private final ActivityResultLauncher<ScanOptions> scanContract = registerForActivityResult(
            new ScanContract(),
            result -> {
                if (result.getContents() == null) {
                    Toast.makeText(getActivity(), "Invalid QR code", Toast.LENGTH_SHORT).show();
                } else {
                    String scannedMessage = result.getContents();
                    requireActivity().getSupportFragmentManager().popBackStack();
                    ((EventListActivity) requireActivity()).handleQRCodeResult(scannedMessage);
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qr_scanner, container, false);

        // Check for camera permission
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
        } else {
            startQRScanner();
        }

        return view;
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
            Toast.makeText(getActivity(), "Camera permission is needed to scan QR codes", Toast.LENGTH_SHORT).show();
        }
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    }

    private void startQRScanner() {
        // Configure ScanOptions
        ScanOptions options = new ScanOptions()
                .setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                .setPrompt("Scan a QR code")
                .setBeepEnabled(true)
                .setCameraId(0);  // Use a specific camera of the device

        // Start the QR scanner
        scanContract.launch(options);
    }
}

