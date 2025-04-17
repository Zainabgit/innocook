package com.example.myapp.ApiRequests;



import com.example.myapp.Models.DetailedRecipe;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CallDetailedRecipe {
    @GET("recipes/{id}/information")
    Call<DetailedRecipe> callDetailedRecipe(
            @Path("id") Integer id,
            @Query("includeNutrition") Boolean includeNutrition
    );
}
