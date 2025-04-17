package com.example.myapp.Listeners;

import com.example.myapp.Models.DetailedRecipe;

import java.util.List;

public interface DetailedRecipeResponseBulkListener {
    void didFetch(List<DetailedRecipe> response, String message);
    void didError(String message);
}
