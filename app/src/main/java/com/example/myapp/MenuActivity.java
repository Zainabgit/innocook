package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.menu.bottom_nav_menu);
    }
}