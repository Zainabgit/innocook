package com.example.myapp.Listeners;

import com.example.myapp.Models.ListOfRecipesResponse;

public interface ListOfRecipesResponseListener {
    void didFetch(ListOfRecipesResponse response, String message);
    void didError(String message);
}
