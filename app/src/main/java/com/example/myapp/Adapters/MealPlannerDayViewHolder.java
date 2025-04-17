package com.example.myapp.Adapters;


import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;

public class MealPlannerDayViewHolder extends RecyclerView.ViewHolder{

    LinearLayout MealPlanDayLayout;
    TextView txtdayName;


    public MealPlannerDayViewHolder(@NonNull View itemView) {
        super(itemView);
        MealPlanDayLayout = itemView.findViewById(R.id.MealPlanDayLayout);
        txtdayName = itemView.findViewById(R.id.txtdayName);
    }
}
