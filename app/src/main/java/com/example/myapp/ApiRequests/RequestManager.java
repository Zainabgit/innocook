package com.example.myapp.ApiRequests;

import android.content.Context;
import android.content.res.Resources;

import com.example.myapp.Listeners.DetailedRecipeResponseBulkListener;
import com.example.myapp.Listeners.DetailedRecipeResponseListener;
import com.example.myapp.Listeners.ListOfMealPlannerDayResponseListener;
import com.example.myapp.Listeners.ListOfMealPlannerWeekResponseListener;
import com.example.myapp.Listeners.ListOfRecipesResponseListener;
import com.example.myapp.Models.Day;
import com.example.myapp.Models.DetailedRecipe;
import com.example.myapp.Models.ListOfRecipesResponse;
import com.example.myapp.Models.MealPlanner;
import com.example.myapp.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestManager {
    Context context;
    Resources resources;
    String apiKey;

    // Create OkHttp client with an interceptor for adding headers
    OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(chain -> {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .header("x-api-key", apiKey) // Replace "YOUR_API_KEY" with your actual API key
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            })
            .build();
    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://api.spoonacular.com/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(client)
                            .build();

    public RequestManager(Context context) {
        this.context = context;
        resources = context.getResources();
        apiKey = resources.getString(R.string.api_key);
    }

    public void getRecipeList(ListOfRecipesResponseListener listener,
                              String include,
                              String exclude,
                              String type,
                              String cuisine,
                              String diet,
                              Integer timeRange,
                              Integer maxCarbs,
                              Integer maxProtein,
                              Integer maxCalories,
                              Integer maxFat){
        CallRecipes callRecipes = retrofit.create(CallRecipes.class);
        Call<ListOfRecipesResponse> call = callRecipes.callListOfRecipes(include,exclude,type,cuisine,diet,timeRange,maxCarbs,maxProtein,maxCalories,maxFat);
//        Call<ListOfRecipesResponse> call = callRecipes.callListOfRecipes(include);
        call.enqueue(new Callback<ListOfRecipesResponse>() {
            @Override
            public void onResponse(Call<ListOfRecipesResponse> call, Response<ListOfRecipesResponse> response) {
                if(!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }
                else{
                    listener.didFetch(response.body(),response.message());
                }
            }

            @Override
            public void onFailure(Call<ListOfRecipesResponse> call, Throwable throwable) {
                listener.didError(throwable.getMessage());
            }
        });
    }

    public void getMealDayPlanner(ListOfMealPlannerDayResponseListener listener,
                               String timeFrame,
                               Integer maxCalories,
                               String diet,
                               String exclude){
        CallMealDayPlanner callMealDayPlanner = retrofit.create(CallMealDayPlanner.class);
        Call<Day> call = callMealDayPlanner.callMealPlanner(timeFrame,maxCalories,diet,exclude);
//        Call<ListOfRecipesResponse> call = callRecipes.callListOfRecipes(include);
        call.enqueue(new Callback<Day>() {
            @Override
            public void onResponse(Call<Day> call, Response<Day> response) {
                if(!response.isSuccessful()){
                    listener.didError(response.message());
                }
                else{
                    listener.didFetch(response.body(),response.message());
                }
            }

            @Override
            public void onFailure(Call<Day> call, Throwable throwable) {
                listener.didError(throwable.getMessage());
            }
        });
    }

    public void getMealWeekPlanner(ListOfMealPlannerWeekResponseListener listener,
                                  String timeFrame,
                                  Integer maxCalories,
                                  String diet,
                                  String exclude){
        CallMealPlanner callMealPlanner = retrofit.create(CallMealPlanner.class);
        Call<MealPlanner> call = callMealPlanner.callMealPlanner(timeFrame,maxCalories,diet,exclude);
//        Call<ListOfRecipesResponse> call = callRecipes.callListOfRecipes(include);
        call.enqueue(new Callback<MealPlanner>() {
            @Override
            public void onResponse(Call<MealPlanner> call, Response<MealPlanner> response) {
                if(!response.isSuccessful()){
                    listener.didError(response.message());
                }
                else{
                    listener.didFetch(response.body(),response.message());
                }
            }

            @Override
            public void onFailure(Call<MealPlanner> call, Throwable throwable) {
                listener.didError(throwable.getMessage());
            }
        });
    }

    public void getDetailedRecipe(DetailedRecipeResponseListener listener,
                                  Integer id){
        CallDetailedRecipe callDetailedRecipe = retrofit.create(CallDetailedRecipe.class);
        Call<DetailedRecipe> call = callDetailedRecipe.callDetailedRecipe(id,true);
//        Call<ListOfRecipesResponse> call = callRecipes.callListOfRecipes(include);
        call.enqueue(new Callback<DetailedRecipe>() {
            @Override
            public void onResponse(Call<DetailedRecipe> call, Response<DetailedRecipe> response) {
                if(!response.isSuccessful()){
                    listener.didError(response.message());
                }
                else{
                    listener.didFetch(response.body(),response.message());
                }
            }

            @Override
            public void onFailure(Call<DetailedRecipe> call, Throwable throwable) {
                listener.didError(throwable.getMessage());
            }
        });
    }

    public void getDetailedRecipeBulk(DetailedRecipeResponseBulkListener listener, String ids) throws UnsupportedEncodingException {

//        String encodedIds = URLEncoder.encode(ids, StandardCharsets.UTF_8.toString());
//
        CallDetailedRecipeBulk callDetailedRecipeBulk = retrofit.create(CallDetailedRecipeBulk.class);
        Call<List<DetailedRecipe>> call = callDetailedRecipeBulk.callDetailedRecipe(ids, false);
        call.enqueue(new Callback<List<DetailedRecipe>>() {
            @Override
            public void onResponse(Call<List<DetailedRecipe>> call, Response<List<DetailedRecipe>> response) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                } else {
                    listener.didFetch(response.body(), response.message());
                }
            }

            @Override
            public void onFailure(Call<List<DetailedRecipe>> call, Throwable throwable) {
                listener.didError(throwable.getMessage());
            }
        });
    }
}
