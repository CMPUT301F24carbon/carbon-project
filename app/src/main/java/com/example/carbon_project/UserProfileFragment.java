package com.example.carbon_project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

// To access this fragment, always set the TextView textViewProfile as the first letter of the user name
// To access this fragment, always set the TextView textViewProfile as the first letter of the user name
// To access this fragment, always set the TextView textViewProfile as the first letter of the user name
// To access this fragment, always set the TextView textViewProfile as the first letter of the user name
// To access this fragment, always set the TextView textViewProfile as the first letter of the user name

public class UserProfileFragment extends Fragment {
    private EditText editTextName, editTextEmail, editTextPhoneNumber, editTextFacility;
    private MaterialButton buttonEditProfile, buttonSaveProfile;
    private Button buttonUpload;
    private ImageView imageViewProfile;
    private TextView textViewProfile;

    // Launcher to handle the result of the image selection
    private ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        // Set the selected image to the ImageView
                        imageViewProfile.setImageURI(selectedImageUri);
                        // Save the image uri to firebase
                        // Save the image uri to firebase
                        // Save the image uri to firebase
                        // Save the image uri to firebase
                        // Save the image uri to firebase
                    }
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_profile, container, false);

        editTextName = view.findViewById(R.id.editText_name);
        editTextEmail = view.findViewById(R.id.editText_email);
        editTextPhoneNumber = view.findViewById(R.id.editText_phone_number);
        editTextFacility = view.findViewById(R.id.editText_facility);   // Treat this as a button
        buttonEditProfile = view.findViewById(R.id.button_edit_profile);
        buttonSaveProfile = view.findViewById(R.id.button_save_profile);
        buttonUpload = view.findViewById(R.id.button_upload);
        imageViewProfile = view.findViewById(R.id.imageView_profile_image);
        textViewProfile = view.findViewById(R.id.textView_profile_image);

        // Check if a new image was passed through arguments
        Bundle args = getArguments();
        if (args != null && args.containsKey("profile_image_uri")) {
            Uri imageUri = args.getParcelable("profile_image_uri");
            imageViewProfile.setImageURI(imageUri);  // Set the new image
        } else {
            // Set the text profile image visible if no image URI is provided
            imageViewProfile.setVisibility(View.GONE);
            textViewProfile.setVisibility(View.VISIBLE);
        }

        // Set click listener for the edit button
        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make EditText fields editable
                editTextName.setEnabled(true);
                editTextEmail.setEnabled(true);
                editTextPhoneNumber.setEnabled(true);

                // Make editText_facility clickable
                editTextFacility.setClickable(true);
                editTextFacility.setFocusable(true);

                // Switch visibility of buttons
                buttonEditProfile.setVisibility(View.GONE);
                buttonSaveProfile.setVisibility(View.VISIBLE);
                buttonUpload.setVisibility(View.VISIBLE);
            }
        });

        // Set click listener for the save button
        buttonSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make EditText fields non-editable after saving
                editTextName.setEnabled(false);
                editTextEmail.setEnabled(false);
                editTextPhoneNumber.setEnabled(false);

                // Make editText_facility non-clickable
                editTextFacility.setClickable(false);
                editTextFacility.setFocusable(false);

                // Switch visibility of buttons
                buttonSaveProfile.setVisibility(View.GONE);
                buttonEditProfile.setVisibility(View.VISIBLE);
                buttonUpload.setVisibility(View.GONE);

                // Add code here to save changes to firebase
                // Add code here to save changes to firebase
                // Add code here to save changes to firebase
                // Add code here to save changes to firebase
                // Add code here to save changes to firebase
            }
        });

        // Set click listener for the editText_facility
        editTextFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pop up a dialog or fragment to add / edit facility
                // Pop up a dialog or fragment to add / edit facility
                // Pop up a dialog or fragment to add / edit facility
                // Pop up a dialog or fragment to add / edit facility
                // Pop up a dialog or fragment to add / edit facility
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch intent to select an image from the gallery
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                imagePickerLauncher.launch(intent);
            }
        });

        return view;
    }
}
