package com.example.myapp;

public class FirebaseAuth {
    private static FirebaseAuth instance;

    // Private constructor to prevent instantiation from outside
    private FirebaseAuth() {
        // Initialize any necessary variables or setup here if needed
    }

    // Method to get the instance of FirebaseAuth
    public static FirebaseAuth getInstance() {
        // Check if the instance is null, create a new instance if it's null
        if (instance == null) {
            instance = new FirebaseAuth();
        }
        return instance;
    }
}

