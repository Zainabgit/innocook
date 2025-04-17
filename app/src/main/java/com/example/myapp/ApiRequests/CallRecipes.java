package com.example.myapp.ApiRequests;

import com.example.myapp.Models.ListOfRecipesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CallRecipes {
    @GET("recipes/complexSearch")
    Call<ListOfRecipesResponse> callListOfRecipes(
            @Query("includeIngredients") String includes,
            @Query("excludeIngredients") String excludes,
            @Query("type") String type,
            @Query("cuisine") String cuisine,
            @Query("diet") String diet,
            @Query("maxReadyTime") Integer maxReadyTime,
            @Query("maxCarbs") Integer maxCarbs,
            @Query("maxProtein") Integer maxProtein,
            @Query("maxCalories") Integer maxCalories,
            @Query("maxCalories") Integer maxFat

    );
}
