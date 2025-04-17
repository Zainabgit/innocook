package com.example.myapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.Adapters.RecipeListAdapter;
import com.example.myapp.ApiRequests.RequestManager;
import com.example.myapp.Listeners.ListOfRecipesResponseListener;
import com.example.myapp.Models.ListOfRecipesResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class PantryRecipesActivity extends AppCompatActivity {

    ProgressDialog dialog;
    RequestManager manager;
    RecipeListAdapter recipeListAdapter;
    RecyclerView recyclerView;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_recipes);

        dialog = new ProgressDialog(PantryRecipesActivity.this);
        dialog.setTitle("Loading....");

        bottomNavigationView = findViewById(R.id.bottom_navigation_pantry);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onOptionsItemSelected);

        manager = new RequestManager(PantryRecipesActivity.this);
        String includes = getIntent().getStringExtra("includes");
        if(includes.equals("")){
            includes = null;
        }
        String excludes = getIntent().getStringExtra("excludes");
        if(excludes.equals("")){
            excludes = null;
        }
        String type = getIntent().getStringExtra("type");
        if(type.equals("Any")){
            type = null;
        }
        String cuisine = getIntent().getStringExtra("cuisine");
        if(cuisine.equals("Any")){
            cuisine = null;
        }
        String diet = getIntent().getStringExtra("diet");
        if(diet.equals("Any")){
            diet = null;
        }
        Integer timeRange = getIntent().getIntExtra("timeRange",0);
        if(timeRange == 0){
            timeRange = null;
        }

        manager.getRecipeList(listOfRecipesResponseListener,includes,excludes,type,cuisine,diet,timeRange,null,null,null,null);
        dialog.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId(); // Get the item ID

        bottomNavigationView.getMenu().findItem(itemId).setChecked(true);

        if (itemId == R.id.home) {
            Intent intent = new Intent(PantryRecipesActivity.this, CameraActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.proportion) {
            Intent intent = new Intent(PantryRecipesActivity.this, ProportionProActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.planner) {
            Intent intent = new Intent(PantryRecipesActivity.this, MealPlannerActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.profile) {
            Intent intent = new Intent(PantryRecipesActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    private final ListOfRecipesResponseListener listOfRecipesResponseListener = new ListOfRecipesResponseListener() {
        @Override
        public void didFetch(ListOfRecipesResponse response, String message) {
            dialog.dismiss();
            recyclerView = findViewById(R.id.recyler_recipeList);
            recyclerView.setHasFixedSize(true);
            if (response.results != null && !response.results.isEmpty()) {
                recyclerView.setLayoutManager(new GridLayoutManager(PantryRecipesActivity.this,1));
                recipeListAdapter = new RecipeListAdapter(PantryRecipesActivity.this, response.results);
                recyclerView.setAdapter(recipeListAdapter);
            }
            else{
                TextView textViewMessage = findViewById(R.id.textViewMessageNoRecipe);
                LinearLayout layout = findViewById(R.id.noRecipeAvailableLayout);
                layout.setVisibility(View.VISIBLE);
                textViewMessage.setVisibility(View.VISIBLE);
                textViewMessage.setText("No recipes found.");
            }

        }

        @Override
        public void didError(String message) {
            Toast.makeText(PantryRecipesActivity.this,message,Toast.LENGTH_SHORT);
        }
    };
}
