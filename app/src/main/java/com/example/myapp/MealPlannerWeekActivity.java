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
import com.example.myapp.Listeners.ListOfMealPlannerWeekResponseListener;
import com.example.myapp.Listeners.ListOfRecipesResponseListener;
import com.example.myapp.Models.ListOfRecipesResponse;
import com.example.myapp.Models.MealPlanner;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MealPlannerWeekActivity extends AppCompatActivity {

    ProgressDialog dialog;
    RequestManager manager;
    MealPlannerAdapter mealPlannerAdapter;
    RecyclerView recyler_Sunday;
    RecyclerView recyler_Monday;
    RecyclerView recyler_Tuesday;
    RecyclerView recyler_Wednesday;
    RecyclerView recyler_Thursday;
    RecyclerView recyler_Friday;
    RecyclerView recyler_Saturday;

    LinearLayout MondayContainer;
    LinearLayout TuesdayContainer;
    LinearLayout WednesdayContainer;
    LinearLayout ThursdayContainer;
    LinearLayout FridayContainer;
    LinearLayout SaturdayContainer;
    LinearLayout SundayContainer;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_planner_week_page);

        dialog = new ProgressDialog(MealPlannerWeekActivity.this);
        dialog.setTitle("Loading....");

        manager = new RequestManager(MealPlannerWeekActivity.this);

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

        manager.getMealWeekPlanner(listOfMealPlannerWeekResponseListener,mealPlan,calories,diet,excludes);
        dialog.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId(); // Get the item ID

        bottomNavigationView.getMenu().findItem(itemId).setChecked(true);

        if (itemId == R.id.home) {
            Intent intent = new Intent(MealPlannerWeekActivity.this, CameraActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.proportion) {
            Intent intent = new Intent(MealPlannerWeekActivity.this, ProportionProActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.planner) {
            Intent intent = new Intent(MealPlannerWeekActivity.this, MealPlannerActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.profile) {
            Intent intent = new Intent(MealPlannerWeekActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private final ListOfMealPlannerWeekResponseListener listOfMealPlannerWeekResponseListener = new ListOfMealPlannerWeekResponseListener() {

        @Override
        public void didFetch(MealPlanner response, String message) {
            dialog.dismiss();
            recyler_Monday = findViewById(R.id.recyler_Monday);
            MondayContainer = findViewById(R.id.MondayContainer);
            recyler_Monday.setHasFixedSize(true);

            recyler_Tuesday = findViewById(R.id.recyler_Tuesday);
            TuesdayContainer = findViewById(R.id.TuesdayContainer);
            recyler_Tuesday.setHasFixedSize(true);

            recyler_Wednesday = findViewById(R.id.recyler_Wednesday);
            WednesdayContainer = findViewById(R.id.WednesdayContainer);
            recyler_Wednesday.setHasFixedSize(true);

            recyler_Thursday = findViewById(R.id.recyler_Thursday);
            ThursdayContainer = findViewById(R.id.ThursdayContainer);
            recyler_Thursday.setHasFixedSize(true);

            recyler_Friday = findViewById(R.id.recyler_Friday);
            FridayContainer = findViewById(R.id.FridayContainer);
            recyler_Friday.setHasFixedSize(true);

            recyler_Saturday = findViewById(R.id.recyler_Saturday);
            SaturdayContainer = findViewById(R.id.SaturdayContainer);
            recyler_Saturday.setHasFixedSize(true);

            recyler_Sunday = findViewById(R.id.recyler_Sunday);
            SundayContainer = findViewById(R.id.SundayContainer);
            recyler_Sunday.setHasFixedSize(true);

            if (response.week != null) {
                recyler_Monday.setLayoutManager(new GridLayoutManager(MealPlannerWeekActivity.this, 1));
                mealPlannerAdapter = new MealPlannerAdapter(MealPlannerWeekActivity.this, response.week.monday.meals);
                recyler_Monday.setAdapter(mealPlannerAdapter);
                MondayContainer.setVisibility(View.VISIBLE);

                recyler_Tuesday.setLayoutManager(new GridLayoutManager(MealPlannerWeekActivity.this, 1));
                mealPlannerAdapter = new MealPlannerAdapter(MealPlannerWeekActivity.this, response.week.tuesday.meals);
                recyler_Tuesday.setAdapter(mealPlannerAdapter);
                TuesdayContainer.setVisibility(View.VISIBLE);

                recyler_Wednesday.setLayoutManager(new GridLayoutManager(MealPlannerWeekActivity.this, 1));
                mealPlannerAdapter = new MealPlannerAdapter(MealPlannerWeekActivity.this, response.week.wednesday.meals);
                recyler_Wednesday.setAdapter(mealPlannerAdapter);
                WednesdayContainer.setVisibility(View.VISIBLE);

                recyler_Thursday.setLayoutManager(new GridLayoutManager(MealPlannerWeekActivity.this, 1));
                mealPlannerAdapter = new MealPlannerAdapter(MealPlannerWeekActivity.this, response.week.thursday.meals);
                recyler_Thursday.setAdapter(mealPlannerAdapter);
                ThursdayContainer.setVisibility(View.VISIBLE);

                recyler_Friday.setLayoutManager(new GridLayoutManager(MealPlannerWeekActivity.this, 1));
                mealPlannerAdapter = new MealPlannerAdapter(MealPlannerWeekActivity.this, response.week.friday.meals);
                recyler_Friday.setAdapter(mealPlannerAdapter);
                FridayContainer.setVisibility(View.VISIBLE);

                recyler_Saturday.setLayoutManager(new GridLayoutManager(MealPlannerWeekActivity.this, 1));
                mealPlannerAdapter = new MealPlannerAdapter(MealPlannerWeekActivity.this, response.week.saturday.meals);
                recyler_Saturday.setAdapter(mealPlannerAdapter);
                SaturdayContainer.setVisibility(View.VISIBLE);

                recyler_Sunday.setLayoutManager(new GridLayoutManager(MealPlannerWeekActivity.this, 1));
                mealPlannerAdapter = new MealPlannerAdapter(MealPlannerWeekActivity.this, response.week.sunday.meals);
                recyler_Sunday.setAdapter(mealPlannerAdapter);
                SundayContainer.setVisibility(View.VISIBLE);
            } else {
                TextView textViewMessage = findViewById(R.id.textViewMessageNoRecipe);
                LinearLayout layout = findViewById(R.id.noRecipeAvailableLayout);
                layout.setVisibility(View.VISIBLE);
                textViewMessage.setVisibility(View.VISIBLE);
                textViewMessage.setText("No recipes found.");
            }

            }


            @Override
            public void didError (String message){
                Toast.makeText(MealPlannerWeekActivity.this, message, Toast.LENGTH_SHORT);
            }
        };
    }



