package com.example.myapp.ApiRequests;



import com.example.myapp.Models.DetailedRecipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CallDetailedRecipeBulk {
    @GET("recipes/informationBulk")
    Call<List<DetailedRecipe>> callDetailedRecipe(
            @Query("ids") String ids,
            @Query("includeNutrition") Boolean includeNutrition
    );
}
