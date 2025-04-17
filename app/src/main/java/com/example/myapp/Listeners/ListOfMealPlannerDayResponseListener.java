package com.example.myapp.Listeners;

import com.example.myapp.Models.Day;

public interface ListOfMealPlannerDayResponseListener {
    void didFetch(Day response, String message);
    void didError(String message);
}
