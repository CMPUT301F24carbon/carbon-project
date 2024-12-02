package com.example.carbon_project.Controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carbon_project.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * The SaveQRCodeActivity class is an activity that saves a QR code to a file.
 */
public class SaveQRCodeActivity extends AppCompatActivity {

    private ImageView qrCodeImageView;
    private Button saveButton;

    /**
     * Called when the activity is starting.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_qr_code);

        qrCodeImageView = findViewById(R.id.qrCodeImageView);
        saveButton = findViewById(R.id.saveButton);

        // Retrieve the byte array from the Bundle
        byte[] qrCodeByteArray = getIntent().getByteArrayExtra("qrCodeByteArray");
        if (qrCodeByteArray != null) {
            Bitmap qrCodeBitmap = BitmapFactory.decodeByteArray(qrCodeByteArray, 0, qrCodeByteArray.length);
            qrCodeImageView.setImageBitmap(qrCodeBitmap);
        } else {
            Toast.makeText(this, "No QR code found", Toast.LENGTH_SHORT).show();
        }

        // Set up the Save button click listener
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qrCodeByteArray != null) {
                    saveQRCodeToFile(qrCodeByteArray);
                } else {
                    Toast.makeText(SaveQRCodeActivity.this, "No QR code to save", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Saves the QR code to a file.
     * @param qrCodeByteArray
     */
    private void saveQRCodeToFile(byte[] qrCodeByteArray) {
        // Convert byte array to Bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(qrCodeByteArray, 0, qrCodeByteArray.length);

        // Save Bitmap to file
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CarbonProject");
        if (!directory.exists()) {
            directory.mkdirs(); // Create directory if it doesn't exist
        }

        File file = new File(directory, "qrcode_" + System.currentTimeMillis() + ".png");
        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            Toast.makeText(this, "QR Code saved to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Failed to save QR code: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // Return to the organizer dashboard
        Intent intent = new Intent(SaveQRCodeActivity.this, OrganizerDashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

