package com.example.myapp.Listeners;

import com.example.myapp.Models.DetailedRecipe;

public interface DetailedRecipeResponseListener {
    void didFetch(DetailedRecipe response, String message);
    void didError(String message);
}
