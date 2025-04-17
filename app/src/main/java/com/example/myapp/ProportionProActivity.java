package com.example.myapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ProportionProActivity extends Activity {

    List<String> TypeOfMeal;
    Spinner spinnerType;
    ArrayAdapter<String> adapterType;
    String selectedType;
    List<String> TypeOfDiet;
    Spinner spinnerDiet;
    ArrayAdapter<String> adapterDiet;
    String selectedDiet;
    Button generateRecipe;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proportion_pg); // Assuming your XML file is named activity_home_page.xml

        bottomNavigationView = findViewById(R.id.bottom_navigation_pantry);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onOptionsItemSelected);

        generateMealDropDown();

        generateDietDropDown();

        generateRecipe = findViewById(R.id.generateRecipesButton_proportion);

        generateRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                generateRecipeFunction();

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId(); // Get the item ID

        bottomNavigationView.getMenu().findItem(itemId).setChecked(true);

        if (itemId == R.id.home) {
            Intent intent = new Intent(ProportionProActivity.this, CameraActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.proportion) {
            Intent intent = new Intent(ProportionProActivity.this, ProportionProActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.planner) {
            Intent intent = new Intent(ProportionProActivity.this, MealPlannerActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.profile) {
            Intent intent = new Intent(ProportionProActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    private void generateMealDropDown() {
        TypeOfMeal = new ArrayList<>();
        TypeOfMeal.add("Any");
        TypeOfMeal.add("Main course");
        TypeOfMeal.add("Dessert");
        TypeOfMeal.add("Appetizer");
        TypeOfMeal.add("Salad");
        TypeOfMeal.add("Breakfast");
        TypeOfMeal.add("Snack");
        TypeOfMeal.add("Beverage");

        spinnerType = findViewById(R.id.spinnerProportionType);
        adapterType = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, TypeOfMeal);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapterType);

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedType = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
            }
        });
    }
    private void generateDietDropDown() {
        TypeOfDiet = new ArrayList<>();
        TypeOfDiet.add("Any");
        TypeOfDiet.add("Gluten Free");
        TypeOfDiet.add("Ketogenic");
        TypeOfDiet.add("Vegetarian");
        TypeOfDiet.add("Vegan");
        TypeOfDiet.add("Paleo");

        spinnerDiet = findViewById(R.id.spinnerProportionDiet);
        adapterDiet  = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, TypeOfDiet);
        adapterDiet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDiet.setAdapter(adapterDiet);

        spinnerDiet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDiet = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
            }
        });
    }
    private void generateRecipeFunction() {


        EditText editTextCarbohydrates = findViewById(R.id.editTextCarbohydrates);
        String editTextCarbohydratesStr = editTextCarbohydrates.getText().toString();
        Integer carbohydrates;

        if (!TextUtils.isEmpty(editTextCarbohydratesStr)) {
            carbohydrates = Integer.parseInt(editTextCarbohydratesStr);
        } else {
            carbohydrates = -1;
        }

        EditText editTextProteins = findViewById(R.id.editTextProteins);
        String editTextProteinsStr = editTextProteins.getText().toString();
        Integer proteins;

        if (!TextUtils.isEmpty(editTextProteinsStr)) {
            proteins = Integer.parseInt(editTextProteinsStr);
        } else {
            proteins = -1;
        }

        EditText editTextFats = findViewById(R.id.editTextFats);
        String editTextFatsStr = editTextFats.getText().toString();
        Integer fats;

        if (!TextUtils.isEmpty(editTextFatsStr)) {
            fats = Integer.parseInt(editTextFatsStr);
        } else {
            fats = -1;
        }

        EditText editTextCalories = findViewById(R.id.editTextCalories);
        String editTexCaloriesStr = editTextCalories.getText().toString();
        Integer calories;

        if (!TextUtils.isEmpty(editTexCaloriesStr)) {
            calories = Integer.parseInt(editTexCaloriesStr);
        } else {
            calories = -1;
        }

        Intent intent = new Intent(ProportionProActivity.this, PropotionProRecipesActivity.class);
        intent.putExtra("type", selectedType);
        intent.putExtra("diet", selectedDiet);
        intent.putExtra("carbohydrates", carbohydrates);
        intent.putExtra("proteins", proteins);
        intent.putExtra("calories", calories);
        intent.putExtra("fats", fats);
        startActivity(intent);


    }
}
