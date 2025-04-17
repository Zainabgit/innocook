package com.example.myapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.Models.ExtendedIngredient;
import com.example.myapp.Models.Nutrient;
import com.example.myapp.R;

import java.util.List;

public class DetailedRecipeNutrientAdapter extends  RecyclerView.Adapter<DetailRecipeNutrientViewHolder>{

    Context context;
    List<Nutrient> list;

    public DetailedRecipeNutrientAdapter(Context context, List<Nutrient> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DetailRecipeNutrientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DetailRecipeNutrientViewHolder(LayoutInflater.from(context).inflate(R.layout.nutrients,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DetailRecipeNutrientViewHolder holder, int position) {
        final Nutrient nutrient = list.get(position);
        holder.nutrientName.setText(nutrient.name);
        holder.nutrientMeasurement.setText(nutrient.amount + " " + nutrient.unit);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
