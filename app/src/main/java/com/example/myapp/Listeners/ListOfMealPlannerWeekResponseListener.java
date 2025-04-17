package com.example.myapp.Listeners;

import com.example.myapp.Models.MealPlanner;

public interface ListOfMealPlannerWeekResponseListener {
    void didFetch(MealPlanner response, String message);
    void didError(String message);
}
