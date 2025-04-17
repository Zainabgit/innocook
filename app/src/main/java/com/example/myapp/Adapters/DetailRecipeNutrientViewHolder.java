package com.example.myapp.Adapters;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;

public class DetailRecipeNutrientViewHolder extends RecyclerView.ViewHolder{

    LinearLayout nutrientContainer;
    TextView nutrientName;
    TextView nutrientMeasurement;

    public DetailRecipeNutrientViewHolder(@NonNull View itemView) {
        super(itemView);

        nutrientContainer = itemView.findViewById(R.id.nutrientContainer);
        nutrientName = itemView.findViewById(R.id.nutrientName);
        nutrientMeasurement = itemView.findViewById(R.id.nutrientMeasurement);
    }
}
