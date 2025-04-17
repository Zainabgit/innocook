package com.example.myapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.Adapters.DetailedRecipeAdapter;
import com.example.myapp.Adapters.DetailedRecipeNutrientAdapter;
import com.example.myapp.Adapters.RecipeListAdapter;
import com.example.myapp.ApiRequests.RequestManager;
import com.example.myapp.Listeners.DetailedRecipeResponseListener;
import com.example.myapp.Listeners.ListOfRecipesResponseListener;
import com.example.myapp.Models.DetailedRecipe;
import com.example.myapp.Models.Nutrient;
import com.example.myapp.Session.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class DetailedRecipeActivity extends AppCompatActivity {

    ProgressDialog dialog;
    RequestManager manager;
    RecyclerView recyclerView;
    RecyclerView neutrientRecyclerView;
    DetailedRecipeAdapter detailedRecipeListAdapter;

    DetailedRecipeNutrientAdapter detailedRecipeNutrientAdapter;

    TextView textViewRecipeTitle;
    TextView preparationTimeHeader;
    TextView preparationTimeValue;
    TextView servingsHeader;
    TextView servingsValue;
    TextView detailedInstruction;
    ImageView imageViewRecipe;

    TextView ingredientList;

    TextView nutrientsList;
    TextView instructions;
    CardView imageCardContainer;

    ImageView imageViewStar;

    boolean isStarred = false;

    private Integer recipeId;
    HelperClass user;
    SessionManager sessionManager;
    FirebaseDatabase database;
    DatabaseReference usersReference;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantry_recipe_pg);

        dialog = new ProgressDialog(DetailedRecipeActivity.this);
        dialog.setTitle("Loading....");

        sessionManager = new SessionManager(this);
        user = sessionManager.getUser();

        // Initialize Firebase and SessionManager
        database = FirebaseDatabase.getInstance();
        usersReference = database.getReference().child("Users");

        bottomNavigationView = findViewById(R.id.bottom_navigation_pantry);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onOptionsItemSelected);

        manager = new RequestManager(DetailedRecipeActivity.this);

        Integer id = getIntent().getIntExtra("recipeId",0);
        imageViewStar = findViewById(R.id.imageViewStar);
        recipeId = id;
        List<Integer> recipeIds = user.getRecipeIds();
        if (recipeIds != null && recipeIds.contains(recipeId)) {
            isStarred = true;
        } else {
            isStarred = false;
        }
        Drawable starDrawable = ContextCompat.getDrawable(
                DetailedRecipeActivity.this,
                isStarred ? R.drawable.ic_star_filled : R.drawable.ic_star_border
        );
        imageViewStar.setImageDrawable(starDrawable);

        imageViewStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStarred = !isStarred;
                Drawable starDrawable = ContextCompat.getDrawable(
                        DetailedRecipeActivity.this,
                        isStarred ? R.drawable.ic_star_filled : R.drawable.ic_star_border
                );
                imageViewStar.setImageDrawable(starDrawable);

                // Capture the recipe ID
                captureRecipeId(recipeId);
            }
        });


        manager.getDetailedRecipe(detailedRecipeResponseListener, id);
        dialog.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId(); // Get the item ID

        bottomNavigationView.getMenu().findItem(itemId).setChecked(true);

        if (itemId == R.id.home) {
            Intent intent = new Intent(DetailedRecipeActivity.this, CameraActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.proportion) {
            Intent intent = new Intent(DetailedRecipeActivity.this, ProportionProActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.planner) {
            Intent intent = new Intent(DetailedRecipeActivity.this, MealPlannerActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.profile) {
            Intent intent = new Intent(DetailedRecipeActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void captureRecipeId(Integer recipeId) {
        if (user != null) {
            String userEmail = user.getEmail().replace(".", "_");

            // Add recipeId to user's list if not already present
            if (user.getRecipeIds() == null) {
                user.setRecipeIds(new ArrayList<>());
            }
            if (!user.getRecipeIds().contains(recipeId)) {
                user.addRecipeId(recipeId);

                // Update user data in Firebase
                usersReference.child(userEmail).setValue(user)
                        .addOnSuccessListener(aVoid -> showToast("Recipe added to favourites successfully"))
                        .addOnFailureListener(e -> showToast("Failed to save recipe ID: " + e.getMessage()));
                sessionManager.saveUser(user);
            } else {
                user.getRecipeIds().remove(recipeId);
                usersReference.child(userEmail).setValue(user)
                        .addOnSuccessListener(aVoid -> showToast("Recipe removed from favourites successfully"))
                        .addOnFailureListener(e -> showToast("Failed to remove recipe ID: " + e.getMessage()));
                sessionManager.saveUser(user);
            }


        } else {
            showToast("User not logged in");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private final DetailedRecipeResponseListener detailedRecipeResponseListener = new DetailedRecipeResponseListener() {

        @Override
        public void didFetch(DetailedRecipe response, String message) {
            dialog.dismiss();
            recipeId = response.id;

            recyclerView = findViewById(R.id.recyler_ingredientList);
            recyclerView.setHasFixedSize(true);
            neutrientRecyclerView = findViewById(R.id.recyler_nutrientsList);
            neutrientRecyclerView.setHasFixedSize(true);
            if (response.id != null) {
                String[] desiredNutrients = {"Calories", "Fat", "Carbohydrates", "Sugar", "Protein"};

                List<Nutrient> filteredIngredients = response.nutrition.nutrients.stream()
                        .filter(nutrient -> Arrays.asList(desiredNutrients).contains(nutrient.name))
                        .map(nutrient -> {
                            Nutrient nutrientElement = new Nutrient();
                            nutrientElement.name = nutrient.name;
                            nutrientElement.unit = nutrient.unit;
                            nutrientElement.amount = nutrient.amount;
                            nutrientElement.percentOfDailyNeeds = nutrient.percentOfDailyNeeds;
                            return nutrientElement;
                        })
                        .collect(Collectors.toList());

                recyclerView.setLayoutManager(new GridLayoutManager(DetailedRecipeActivity.this,1));
                detailedRecipeListAdapter = new DetailedRecipeAdapter(DetailedRecipeActivity.this, response.extendedIngredients);
                recyclerView.setAdapter(detailedRecipeListAdapter);
                ingredientList = findViewById(R.id.ingredientList);
                ingredientList.setVisibility(View.VISIBLE);


                neutrientRecyclerView.setLayoutManager(new GridLayoutManager(DetailedRecipeActivity.this,1));
                detailedRecipeNutrientAdapter = new DetailedRecipeNutrientAdapter(DetailedRecipeActivity.this, filteredIngredients);
                neutrientRecyclerView.setAdapter(detailedRecipeNutrientAdapter);
                nutrientsList = findViewById(R.id.nutrientsList);
                nutrientsList.setVisibility(View.VISIBLE);


            }
            imageViewStar = findViewById(R.id.imageViewStar);
            imageViewStar.setVisibility(View.VISIBLE);

            textViewRecipeTitle = findViewById(R.id.textViewRecipeTitle);
            textViewRecipeTitle.setText(response.title);

            preparationTimeHeader = findViewById(R.id.preparationTimeHeader);
            preparationTimeHeader.setVisibility(View.VISIBLE);

            preparationTimeValue = findViewById(R.id.preparationTimeValue);
            preparationTimeValue.setText(response.readyInMinutes + " " + "min");

            servingsHeader = findViewById(R.id.servingsHeader);
            servingsHeader.setVisibility(View.VISIBLE);

            servingsValue = findViewById(R.id.servingsValue);
            servingsValue.setText(String.valueOf(response.servings));

            instructions = findViewById(R.id.instructions);
            instructions.setVisibility(View.VISIBLE);

            detailedInstruction = findViewById(R.id.detailedInstruction);
            String formattedInstructions = Html.fromHtml(response.instructions).toString();
            for (int i = 0; i < 4; i++) {
                formattedInstructions += "\n";
            }
            detailedInstruction.setText(formattedInstructions);

            imageCardContainer = findViewById(R.id.imageCardContainer);
            imageCardContainer.setVisibility(View.VISIBLE);
            imageViewRecipe = findViewById(R.id.imageViewRecipe);
            Picasso.get().load(response.image).into(imageViewRecipe);

        }

        @Override
        public void didError(String message) {
            Toast.makeText(DetailedRecipeActivity.this,message,Toast.LENGTH_SHORT);
        }
    };


}