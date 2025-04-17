package com.example.myapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.DetailedRecipeActivity;
import com.example.myapp.Models.Result;
import com.example.myapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeListAdapter extends  RecyclerView.Adapter<RecipeListViewHolder> {
    Context context;
    List<Result> list;

    public RecipeListAdapter(Context context, List<Result> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecipeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipeListViewHolder(LayoutInflater.from(context).inflate(R.layout.recipe,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListViewHolder holder, int position) {
        final Result recipe = list.get(position);

        holder.textViewRecipeName.setText(list.get(position).title);
        holder.textViewRecipeName.setSelected(true);
        Picasso.get().load(list.get(position).image).into(holder.imageViewRecipe);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to detail recipe activity
                Intent intent = new Intent(context, DetailedRecipeActivity.class);
                intent.putExtra("recipeId", recipe.id); // Pass the recipe object to detail activity
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {

        return list.size();
    }
}

class RecipeListViewHolder extends RecyclerView.ViewHolder{

    CardView recipeHeaderContainer;
    ImageView imageViewRecipe;
    TextView textViewRecipeName;

    public RecipeListViewHolder(@NonNull View itemView) {
        super(itemView);
        recipeHeaderContainer = itemView.findViewById(R.id.recipeHeaderContainer);
        imageViewRecipe = itemView.findViewById(R.id.imageViewRecipe);
        textViewRecipeName = itemView.findViewById(R.id.textViewRecipeName);
    }
}
