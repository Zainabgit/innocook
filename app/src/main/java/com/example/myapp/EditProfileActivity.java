package com.example.myapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapp.Session.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.Manifest;
import android.widget.Toast;

import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {

    SessionManager sessionManager;
    TextView editTextName, editAddress;
    Button buttonUploadPicture, buttonSave;

    ImageView imageViewProfilePic;
    HelperClass user;

    Uri imageUri;
    FirebaseDatabase database;
    DatabaseReference usersReference;

    BottomNavigationView bottomNavigationView;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int READ_EXTERNAL_STORAGE_PERMISSION_CODE = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofile_pg);

        sessionManager = new SessionManager(this);
        user = sessionManager.getUser();

        // Initialize Firebase and SessionManager
        database = FirebaseDatabase.getInstance();
        usersReference = database.getReference().child("Users");

        bottomNavigationView = findViewById(R.id.bottom_navigation_pantry);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onOptionsItemSelected);

        editTextName = findViewById(R.id.editTextName);
        editTextName.setText(user.getFirstname().toString());
        editAddress = findViewById(R.id.editAddress);
        editAddress.setText(user.getLastname().toString());

        buttonSave = findViewById(R.id.buttonSave);
        buttonUploadPicture = findViewById(R.id.buttonUploadPicture);
        imageViewProfilePic  = findViewById(R.id.imageViewProfilePic);

        buttonUploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
                    if (ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, READ_EXTERNAL_STORAGE_PERMISSION_CODE);
                    } else {
                        openImageSelector();
                    }
                } else { // Android 12 and below
                    if (ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_CODE);
                    } else {
                        openImageSelector();
                    }
                }
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = user.getEmail().replace(".", "_");

                user.setFirstname(editTextName.getText().toString());
                user.setLastname(editAddress.getText().toString());
                user.setProfilePicUrl(imageUri.toString());

                // Update user data in Firebase
                usersReference.child(userEmail).setValue(user)
                        .addOnSuccessListener(aVoid -> showToast("Profile updated"))
                        .addOnFailureListener(e -> showToast("Failed to update the profile "));
                sessionManager.saveUser(user);

                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId(); // Get the item ID

        bottomNavigationView.getMenu().findItem(itemId).setChecked(true);

        if (itemId == R.id.home) {
            Intent intent = new Intent(EditProfileActivity.this, CameraActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.proportion) {
            Intent intent = new Intent(EditProfileActivity.this, ProportionProActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.planner) {
            Intent intent = new Intent(EditProfileActivity.this, MealPlannerActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.profile) {
            Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    private void openImageSelector() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a profile picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageSelector();
            } else {
                Toast.makeText(this, "Permission denied to read your Media Images", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
             imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageViewProfilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
