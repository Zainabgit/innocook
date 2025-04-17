package com.example.myapp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.Adapters.RecipeBulkListAdapter;
import com.example.myapp.Adapters.RecipeListAdapter;
import com.example.myapp.ApiRequests.RequestManager;
import com.example.myapp.Listeners.DetailedRecipeResponseBulkListener;
import com.example.myapp.Listeners.DetailedRecipeResponseListener;
import com.example.myapp.Models.DetailedRecipe;
import com.example.myapp.Session.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

public class ProfileActivity extends AppCompatActivity {

    SessionManager sessionManager;
    TextView name, userAddress;
    Button btnEditProfile, buttonLogout;
    HelperClass user;

    ImageView profileImage;
    RequestManager apiManager;

    RecyclerView recyclerView;

    RecipeBulkListAdapter recipeListAdapter;

    Uri imageUri;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_pg);

        apiManager = new RequestManager(ProfileActivity.this);
        sessionManager = new SessionManager(this);
        user = sessionManager.getUser();

        bottomNavigationView = findViewById(R.id.bottom_navigation_pantry);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onOptionsItemSelected);

        name = findViewById(R.id.name);
        name.setText(user.getFirstname().toString() + " " + user.getLastname().toString());

        profileImage = findViewById(R.id.profileImage);
        String profilePicUrl = user.getProfilePicUrl();
        if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
            try {
                imageUri = Uri.parse(profilePicUrl);
                ContentResolver contentResolver = getContentResolver();
                if (contentResolver != null) {
                    InputStream inputStream = contentResolver.openInputStream(imageUri);
                    if (inputStream != null) {
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        if (bitmap != null) {
                            profileImage.setImageBitmap(bitmap);
                        } else {
                            Log.e("Bitmap", "Failed to decode bitmap from input stream");
                        }
                        inputStream.close();
                    } else {
                        Log.e("InputStream", "Failed to open input stream");
                    }
                } else {
                    Log.e("ContentResolver", "ContentResolver is null");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("ImageLoading", "Error loading image: " + e.getMessage());
                // Handle the error appropriately
            }
        } else {
            Log.e("ProfilePicUrl", "Profile picture URL is empty");
        }

        List<Integer> recipeIds = user.getRecipeIds();
        if (recipeIds == null) {
            recipeIds = List.of(); // Set to an empty list to avoid null issues
        }

        String recipeIdsString = recipeIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        try {
            apiManager.getDetailedRecipeBulk(detailedRecipeResponseListener,recipeIdsString);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        btnEditProfile = findViewById(R.id.btnEditProfile);

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        buttonLogout = findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.clearSession();
                Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId(); // Get the item ID

        bottomNavigationView.getMenu().findItem(itemId).setChecked(true);

        if (itemId == R.id.home) {
            Intent intent = new Intent(ProfileActivity.this, CameraActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.proportion) {
            Intent intent = new Intent(ProfileActivity.this, ProportionProActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.planner) {
            Intent intent = new Intent(ProfileActivity.this, MealPlannerActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.profile) {
            Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }



    private final DetailedRecipeResponseBulkListener detailedRecipeResponseListener = new DetailedRecipeResponseBulkListener() {

        @Override
        public void didFetch(List<DetailedRecipe> response, String message) {
            recyclerView = findViewById(R.id.recyclerFavourite);
            recyclerView.setHasFixedSize(true);
            if (!response.isEmpty()) {
                recyclerView.setLayoutManager(new GridLayoutManager(ProfileActivity.this,1));
                recipeListAdapter = new RecipeBulkListAdapter(ProfileActivity.this,response);
                recyclerView.setAdapter(recipeListAdapter);
            }
            else{
                TextView textViewMessage = findViewById(R.id.textViewMessageNoRecipe);
                LinearLayout layout = findViewById(R.id.noRecipeAvailableLayout);
                layout.setVisibility(View.VISIBLE);
                textViewMessage.setVisibility(View.VISIBLE);
                textViewMessage.setText("No favourite recipes available.");
            }
        }

        @Override
        public void didError(String message) {
            Toast.makeText(ProfileActivity.this,message, Toast.LENGTH_SHORT);
        }
    };
}
