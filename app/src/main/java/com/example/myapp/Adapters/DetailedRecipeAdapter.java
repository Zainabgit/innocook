package com.example.myapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.Models.ExtendedIngredient;
import com.example.myapp.Models.Ingredient;
import com.example.myapp.Models.Result;
import com.example.myapp.R;

import java.util.List;

public class DetailedRecipeAdapter extends  RecyclerView.Adapter<DetailRecipeViewHolder>{

    Context context;
    List<ExtendedIngredient> list;

    public DetailedRecipeAdapter(Context context, List<ExtendedIngredient> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DetailRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DetailRecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.ingredients,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DetailRecipeViewHolder holder, int position) {
        final ExtendedIngredient ingredient = list.get(position);
        holder.ingredientName.setText(ingredient.name);
        holder.ingredientMeasurement.setText(ingredient.measures.us.amount + " " + ingredient.measures.us.unitShort);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
