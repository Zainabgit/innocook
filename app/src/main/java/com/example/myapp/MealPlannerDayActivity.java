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

import com.example.myapp.Adapters.MealPlannerAdapter;
import com.example.myapp.Adapters.RecipeListAdapter;
import com.example.myapp.ApiRequests.RequestManager;
import com.example.myapp.Listeners.ListOfMealPlannerDayResponseListener;
import com.example.myapp.Listeners.ListOfRecipesResponseListener;
import com.example.myapp.Models.Day;
import com.example.myapp.Models.ListOfRecipesResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MealPlannerDayActivity extends AppCompatActivity {

    ProgressDialog dialog;
    RequestManager manager;
    MealPlannerAdapter mealPlannerAdapter;
    RecyclerView recyclerViewMeals;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_planner_day_page);

        dialog = new ProgressDialog(MealPlannerDayActivity.this);
        dialog.setTitle("Loading....");

        manager = new RequestManager(MealPlannerDayActivity.this);

        bottomNavigationView = findViewById(R.id.bottom_navigation_pantry);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onOptionsItemSelected);

        String mealPlan = getIntent().getStringExtra("mealPlan");

        String diet = getIntent().getStringExtra("diet");
        if(diet.equals("Any")){
            diet = null;
        }

        Integer calories = getIntent().getIntExtra("calories",-1);
        if(calories == -1){
            calories = null;
        }

        String excludes = getIntent().getStringExtra("excludes");
        if(excludes.equals("")){
            excludes = null;
        }

        manager.getMealDayPlanner(listOfMealPlannerDayResponseListener,mealPlan,calories,diet,excludes);
        dialog.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId(); // Get the item ID

        bottomNavigationView.getMenu().findItem(itemId).setChecked(true);

        if (itemId == R.id.home) {
            Intent intent = new Intent(MealPlannerDayActivity.this, CameraActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.proportion) {
            Intent intent = new Intent(MealPlannerDayActivity.this, ProportionProActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.planner) {
            Intent intent = new Intent(MealPlannerDayActivity.this, MealPlannerActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.profile) {
            Intent intent = new Intent(MealPlannerDayActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private final ListOfMealPlannerDayResponseListener listOfMealPlannerDayResponseListener = new ListOfMealPlannerDayResponseListener() {
        @Override
        public void didFetch(Day response, String message) {
            dialog.dismiss();
            recyclerViewMeals = findViewById(R.id.recyler_Monday);
            recyclerViewMeals.setHasFixedSize(true);
            if (response.meals != null && !response.meals.isEmpty()) {
                recyclerViewMeals.setLayoutManager(new GridLayoutManager(MealPlannerDayActivity.this,1));
                mealPlannerAdapter = new MealPlannerAdapter(MealPlannerDayActivity.this, response.meals);
                recyclerViewMeals.setAdapter(mealPlannerAdapter);
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
            Toast.makeText(MealPlannerDayActivity.this,message,Toast.LENGTH_SHORT);
        }
    };
}
