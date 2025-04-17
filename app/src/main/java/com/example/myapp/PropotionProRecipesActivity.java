package com.example.myapp;

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

public class PropotionProRecipesActivity extends AppCompatActivity {

    ProgressDialog dialog;
    RequestManager manager;
    RecipeListAdapter recipeListAdapter;
    RecyclerView recyclerView;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_recipes);

        dialog = new ProgressDialog(PropotionProRecipesActivity.this);
        dialog.setTitle("Loading....");

        manager = new RequestManager(PropotionProRecipesActivity.this);

        String type = getIntent().getStringExtra("type");
        if(type.equals("Any")){
            type = null;
        }

        String diet = getIntent().getStringExtra("diet");
        if(diet.equals("Any")){
            diet = null;
        }
        Integer carbohydrates = getIntent().getIntExtra("carbohydrates",-1);
        if(carbohydrates == -1){
            carbohydrates = null;
        }

        Integer proteins = getIntent().getIntExtra("proteins",-1);
        if(proteins == -1){
            proteins = null;
        }

        Integer calories = getIntent().getIntExtra("calories",-1);
        if(calories == -1){
            calories = null;
        }
        Integer fats = getIntent().getIntExtra("fats",-1);
        if(fats == -1){
            fats = null;
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation_pantry);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onOptionsItemSelected);

        manager.getRecipeList(listOfRecipesResponseListener,null,null,type,null,diet,null,carbohydrates,proteins,calories,fats);
        dialog.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId(); // Get the item ID

        bottomNavigationView.getMenu().findItem(itemId).setChecked(true);

        if (itemId == R.id.home) {
            Intent intent = new Intent(PropotionProRecipesActivity.this, CameraActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.proportion) {
            Intent intent = new Intent(PropotionProRecipesActivity.this, ProportionProActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.planner) {
            Intent intent = new Intent(PropotionProRecipesActivity.this, MealPlannerActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.profile) {
            Intent intent = new Intent(PropotionProRecipesActivity.this, ProfileActivity.class);
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
                recyclerView.setLayoutManager(new GridLayoutManager(PropotionProRecipesActivity.this,1));
                recipeListAdapter = new RecipeListAdapter(PropotionProRecipesActivity.this, response.results);
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
            Toast.makeText(PropotionProRecipesActivity.this,message,Toast.LENGTH_SHORT);
        }
    };
}
