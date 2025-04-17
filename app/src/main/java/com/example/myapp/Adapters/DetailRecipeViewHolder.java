package com.example.myapp.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;

public class DetailRecipeViewHolder extends RecyclerView.ViewHolder{

    LinearLayout ingredientContainer;
    TextView ingredientName;
    TextView ingredientMeasurement;


    public DetailRecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        ingredientContainer = itemView.findViewById(R.id.ingredientContainer);
        ingredientName = itemView.findViewById(R.id.ingredientName);
        ingredientMeasurement = itemView.findViewById(R.id.ingredientMeasurement);
    }
}
