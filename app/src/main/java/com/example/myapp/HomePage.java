package com.example.myapp;

import static com.example.myapp.R.*;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapp.databinding.HomePgBinding;


public class HomePage extends AppCompatActivity {
    HomePgBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomePgBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

       binding.bottomNavigationView.setOnItemSelectedListener(item ->{

           int itemId = item.getItemId();
           if (itemId == R.id.home) {
               replaceFragment(new HomeFragment());
           }
           else if (itemId == id.proportion) {
               replaceFragment(new ProportionFragment());
           }
           else if (itemId == id.planner) {
               replaceFragment(new PlannerFragment());
           }
           else if (itemId == id.profile) {
               replaceFragment(new ProfileFragment());
           }
           return false;
           });
    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id.frameLayout,fragment);
        fragmentTransaction.commit();
    }


}
