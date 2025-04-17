package com.example.myapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MealPlannerActivity extends Activity {

    List<String> TypeOfDiet;
    Spinner spinnerDiet;
    ArrayAdapter<String> adapterDiet;
    String selectedDiet;

    List<String> TypeOfMealPlan;
    Spinner spinnerMealPlan;
    ArrayAdapter<String> adapterMealPlan;
    String selectedMealPan;


    private GridLayout addExcludingIngContainer;
    private List<String> excludedIngredientList = new ArrayList<>();
    private int excludeIngredientCount = 0;
    Button excludeIngredientButton;
    EditText excludeIngredientEditText;
    Button generateRecipe;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planner_pg); // Assuming your XML file is named activity_home_page.xml

        bottomNavigationView = findViewById(R.id.bottom_navigation_planner);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onOptionsItemSelected);

        generateDietDropDown();

        generateMealPlanDropDown();

        excludingIngredientFunction();

        generateRecipe = findViewById(R.id.generateRecipesButton_planner);

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
            Intent intent = new Intent(MealPlannerActivity.this, CameraActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.proportion) {
            Intent intent = new Intent(MealPlannerActivity.this, ProportionProActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.planner) {
            Intent intent = new Intent(MealPlannerActivity.this, MealPlannerActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.profile) {
            Intent intent = new Intent(MealPlannerActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
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

    private void generateMealPlanDropDown() {
        TypeOfMealPlan = new ArrayList<>();
        TypeOfMealPlan.add("Day");
        TypeOfMealPlan.add("Week");

        spinnerMealPlan = findViewById(R.id.spinnerPlanType);
        adapterMealPlan  = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, TypeOfMealPlan);
        adapterMealPlan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMealPlan.setAdapter(adapterMealPlan);

        spinnerMealPlan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMealPan = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
            }
        });
    }

    private void excludingIngredientFunction() {
        excludeIngredientButton = findViewById(R.id.excludeIngredient_btn);
        excludeIngredientEditText = findViewById(R.id.editTextExcludeIngredient);
        addExcludingIngContainer = findViewById(R.id.excludeIngredient_container);

        excludeIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ingredient = excludeIngredientEditText.getText().toString();
                addExcludeIngredient(ingredient,addExcludingIngContainer);
                // Clear the text from the EditText
                excludeIngredientEditText.setText("");

                // Remove focus from the EditText
                excludeIngredientEditText.clearFocus();
                // Hide the keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(excludeIngredientEditText.getWindowToken(), 0);
            }
        });
    }

    private void addExcludeIngredient(String item, GridLayout container) {
        if (!item.isEmpty()) {
            if(excludeIngredientCount < 9) {
                Button ingredientButton = new Button(this);
                ingredientButton.setText(item);
                excludedIngredientList.add(item);

                // Calculate the row and column indices
                int row = excludeIngredientCount / 3; // Divide by 3 to get the row
                int col = excludeIngredientCount % 3; // Remainder determines the column

                // Set the row and column spans
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.rowSpec = GridLayout.spec(row);
                params.columnSpec = GridLayout.spec(col, 1); // Span 1 column
                ingredientButton.setLayoutParams(params);
                container.addView(ingredientButton);

                excludeIngredientCount++;

                // Add an OnClickListener to remove the button
                ingredientButton.setOnClickListener(v -> {
                    container.removeView(ingredientButton); // Remove from GridLayout
                    excludedIngredientList.remove(item);
                    excludeIngredientCount = 0; // Adjust ingredientCount
                    List<String> tempiexcludeIngredientList = new ArrayList<>(excludedIngredientList);
                    excludedIngredientList.clear();

                    // Remove all views from the container
                    container.removeAllViews();

                    // Re-add buttons in correct order
                    boolean isFirstItem = true;
                    for (String excludeIngredient : tempiexcludeIngredientList) {
                        addExcludeIngredient(excludeIngredient, container);

                    }
                });

            }
            else {
                Toast toast = Toast.makeText(MealPlannerActivity.this, "You can't add more than 9 ingredients.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 0, 0); // Set gravity to top
                toast.show();
            }
        } else {
            Toast toast =Toast.makeText(MealPlannerActivity.this, "Please enter an ingredient.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 0); // Set gravity to top
            toast.show();
        }
    }

    private void generateRecipeFunction() {

        EditText editTextCalories = findViewById(R.id.editTextCalories);
        String editTextCaloriesStr = editTextCalories.getText().toString();
        Integer calories;

        if (!TextUtils.isEmpty(editTextCaloriesStr)) {
            calories = Integer.parseInt(editTextCaloriesStr);
        } else {
            calories = -1;
        }

        StringBuilder excludeItems = new StringBuilder();

        for (int i = 0; i < excludedIngredientList.size() ; i++) {
            if(i != excludedIngredientList.size() -1){
                excludeItems.append(excludedIngredientList.get(i).toLowerCase()).append(",");
            }
            else{
                excludeItems.append(excludedIngredientList.get(i).toLowerCase());
            }
        }

        String excludeItemsString = excludeItems.toString();

        selectedMealPan = selectedMealPan.toLowerCase();

        if (selectedMealPan.equals("day")){
            Intent intent = new Intent(MealPlannerActivity.this, MealPlannerDayActivity.class);
            intent.putExtra("mealPlan", selectedMealPan);
            intent.putExtra("calories", calories);
            intent.putExtra("diet", selectedDiet);
            intent.putExtra("excludes", excludeItemsString);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(MealPlannerActivity.this, MealPlannerWeekActivity.class);
            intent.putExtra("mealPlan", selectedMealPan);
            intent.putExtra("calories", calories);
            intent.putExtra("diet", selectedDiet);
            intent.putExtra("excludes", excludeItemsString);
            startActivity(intent);
        }
    }
}
