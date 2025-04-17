package com.example.myapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class PantryActivity extends Activity {

    private GridLayout addIncludingIngContainer;
    private List<String> includedIngredientList = new ArrayList<>();
    private int includeIngredientCount = 0;
    Button addIngredientButton;
    EditText addIngredientEditText;

    private GridLayout addExcludingIngContainer;
    private List<String> excludedIngredientList = new ArrayList<>();
    private int excludeIngredientCount = 0;
    Button excludeIngredientButton;
    EditText excludeIngredientEditText;

    List<String> TypeOfMeal;
    Spinner spinnerType;
    ArrayAdapter<String> adapterType;
    String selectedType;

    List<String> TypeOfCuisines;
    Spinner spinnerCuisines;
    ArrayAdapter<String> adapterCuisines;
    String selectedCuisines;

    List<String> TypeOfDiet;
    Spinner spinnerDiet;
    ArrayAdapter<String> adapterDiet;
    String selectedDiet;
    Button generateRecipe;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantry_pg);

        bottomNavigationView = findViewById(R.id.bottom_navigation_pantry);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onOptionsItemSelected);

        addingIngredientFunction();

        excludingIngredientFunction();

        generateMealDropDown();

        generateCuisinesDropDown();

        generateDietDropDown();


        generateRecipe = findViewById(R.id.generateRecipesButton_pantry);

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
            Intent intent = new Intent(PantryActivity.this, CameraActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.proportion) {
            Intent intent = new Intent(PantryActivity.this, ProportionProActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.planner) {
            Intent intent = new Intent(PantryActivity.this, MealPlannerActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.profile) {
            Intent intent = new Intent(PantryActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void addingIngredientFunction() {
        addIncludingIngContainer = findViewById(R.id.addIngredient_buttons_container);

        String resultText = getIntent().getStringExtra("prediction");

        predictedIngredient(resultText,addIncludingIngContainer);

        addIngredientButton = findViewById(R.id.addIngredient_btn);
        addIngredientEditText = findViewById(R.id.editTextIngredient);

        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ingredient = addIngredientEditText.getText().toString();
                addIngredient(ingredient,addIncludingIngContainer);
                // Clear the text from the EditText
                addIngredientEditText.setText("");

                // Remove focus from the EditText
                addIngredientEditText.clearFocus();
                // Hide the keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(addIngredientEditText.getWindowToken(), 0);
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

        spinnerType = findViewById(R.id.spinnerType);
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

    private void generateCuisinesDropDown() {
        TypeOfCuisines = new ArrayList<>();
        TypeOfCuisines.add("Any");
        TypeOfCuisines.add("Asian");
        TypeOfCuisines.add("American");
        TypeOfCuisines.add("British");
        TypeOfCuisines.add("Chinese");
        TypeOfCuisines.add("French");
        TypeOfCuisines.add("Indian");
        TypeOfCuisines.add("Italian");
        TypeOfCuisines.add("Japanese");
        TypeOfCuisines.add("Korean");
        TypeOfCuisines.add("Middle Eastern");

        spinnerCuisines = findViewById(R.id.spinnerCuisine);
        adapterCuisines = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, TypeOfCuisines);
        adapterCuisines.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCuisines.setAdapter(adapterCuisines);

        spinnerCuisines.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCuisines = parent.getItemAtPosition(position).toString();

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

        spinnerDiet = findViewById(R.id.spinnerDiet);
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

    private void addIngredient(String item, GridLayout container) {
        if (!item.isEmpty()) {
            if(includeIngredientCount < 9) {
                Button ingredientButton = new Button(this);
                ingredientButton.setText(item);
                includedIngredientList.add(item);

                // Calculate the row and column indices
                int row = includeIngredientCount / 3; // Divide by 3 to get the row
                int col = includeIngredientCount % 3; // Remainder determines the column

                // Set the row and column spans
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.rowSpec = GridLayout.spec(row);
                params.columnSpec = GridLayout.spec(col, 1); // Span 1 column
                ingredientButton.setLayoutParams(params);
                container.addView(ingredientButton);

                includeIngredientCount++;

                // Add an OnClickListener to remove the button
                ingredientButton.setOnClickListener(v -> {
                    container.removeView(ingredientButton); // Remove from GridLayout
                    includedIngredientList.remove(item);
                    includeIngredientCount = 0; // Adjust ingredientCount
                    List<String> tempincludedIngredientList = new ArrayList<>(includedIngredientList);
                    includedIngredientList.clear();

                    // Remove all views from the container
                    container.removeAllViews();

                    // Re-add buttons in correct order
                    boolean isFirstItem = true;
                    for (String includedIngredient : tempincludedIngredientList) {
                        if (isFirstItem) {
                            predictedIngredient(includedIngredient, container);
                            isFirstItem = false;
                        } else {
                            addIngredient(includedIngredient, container);
                        }
                    }
                });

            }
            else {
                Toast toast = Toast.makeText(PantryActivity.this, "You can't add more than 9 ingredients.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 0, 0); // Set gravity to top
                toast.show();
            }
        } else {
            Toast toast =Toast.makeText(PantryActivity.this, "Please enter an ingredient.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 0); // Set gravity to top
            toast.show();
        }
    }

    private void predictedIngredient(String item, GridLayout container) {
                Button ingredientButton = new Button(this);
                ingredientButton.setText(item);
                ingredientButton.setBackgroundColor(Color.rgb(220, 188, 249));
                includedIngredientList.add(item);

                // Calculate the row and column indices
                int row = includeIngredientCount / 3; // Divide by 3 to get the row
                int col = includeIngredientCount % 3; // Remainder determines the column

                // Set the row and column spans
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.rowSpec = GridLayout.spec(row);
                params.columnSpec = GridLayout.spec(col, 1); // Span 1 column
                ingredientButton.setLayoutParams(params);
                container.addView(ingredientButton);

                includeIngredientCount++;

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
                Toast toast = Toast.makeText(PantryActivity.this, "You can't add more than 9 ingredients.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 0, 0); // Set gravity to top
                toast.show();
            }
        } else {
            Toast toast =Toast.makeText(PantryActivity.this, "Please enter an ingredient.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 0); // Set gravity to top
            toast.show();
        }
    }

    private void generateRecipeFunction() {
        StringBuilder includeItems = new StringBuilder();

        for (int i = 0; i < includedIngredientList.size() ; i++) {
            if(i != includedIngredientList.size() -1){
                includeItems.append(includedIngredientList.get(i).toLowerCase()).append(",");
            }
            else{
                includeItems.append(includedIngredientList.get(i).toLowerCase());
            }
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

        String includeItemsString = includeItems.toString();
        String excludeItemsString = excludeItems.toString();
        EditText timeRangeEditText = findViewById(R.id.timeRange);
        String timeRangeStr = timeRangeEditText.getText().toString();
        int timeRange;

        if (!TextUtils.isEmpty(timeRangeStr)) {
            timeRange = Integer.parseInt(timeRangeStr);
            // Now you can use the 'timeRange' integer value
        } else {
            timeRange = 0;
        }
        Intent intent = new Intent(PantryActivity.this, PantryRecipesActivity.class);
        intent.putExtra("includes", includeItemsString);
        intent.putExtra("excludes", excludeItemsString);
        intent.putExtra("type", selectedType);
        intent.putExtra("cuisine", selectedCuisines);
        intent.putExtra("diet", selectedDiet);
        intent.putExtra("timeRange", timeRange);
        startActivity(intent);


    }

}

