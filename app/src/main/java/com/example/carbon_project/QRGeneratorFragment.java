package com.example.carbon_project;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.OutputStream;

//Use this in the previous fragment to generate a QR code
//Fragment qrGeneratorFragment = QRGenerator.newInstance("Event ID Here");
//getSupportFragmentManager().beginTransaction()
//        .replace(R.id.fragment_container, qrGeneratorFragment)
//        .addToBackStack(null)
//        .commit();

public class QRGeneratorFragment extends Fragment {

    private static final int STORAGE_PERMISSION_REQUEST_CODE = 101;
    private static final String ARG_QR_TEXT = "qr_text";  // The text QR code shows
    private ImageView qrCodeImageView;
    private Button saveButton;
    private Bitmap qrCodeBitmap;

    public static QRGeneratorFragment newInstance(String text) {
        QRGeneratorFragment fragment = new QRGeneratorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QR_TEXT, text); // Pass the string as an argument
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qr_generator, container, false);

        qrCodeImageView = view.findViewById(R.id.imageView_QR_code);
        saveButton = view.findViewById(R.id.button_save_QR_code);

        // Retrieve the text argument
        String text = getArguments() != null ? getArguments().getString(ARG_QR_TEXT) : "Hello, World!";
        generateQRCode(text);

        // Save QR Code on button click
        saveButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        STORAGE_PERMISSION_REQUEST_CODE);
            } else {
                saveQRCodeImage();
            }
        });

        return view;
    }

    private void generateQRCode(String text) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            int width = 512, height = 512;
            com.google.zxing.common.BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            qrCodeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    qrCodeBitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            qrCodeImageView.setImageBitmap(qrCodeBitmap);
        } catch (WriterException e) {
            Log.e("QRGeneratorFragment", "QR Code generation error: " + e.getMessage());
        }
    }

    private void saveQRCodeImage() {
        OutputStream outputStream;
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "qr_code_" + System.currentTimeMillis() + ".png");
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/QR_Codes");

        try {
            outputStream = getActivity().getContentResolver().openOutputStream(
                    getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues));

            qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();

            Toast.makeText(getActivity(), "QR Code saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("QRGeneratorFragment", "Error saving QR Code: " + e.getMessage());
        }
    }
}
