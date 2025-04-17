package com.example.myapp.ApiRequests;

import com.example.myapp.Models.ListOfRecipesResponse;
import com.example.myapp.Models.MealPlanner;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CallMealPlanner {
    @GET("mealplanner/generate")
    Call<MealPlanner> callMealPlanner(
            @Query("timeFrame") String timeFrame,
            @Query("maxCalories") Integer maxCalories,
            @Query("diet") String diet,
            @Query("excludeIngredients") String excludes



    );
}
