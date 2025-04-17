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
import com.example.myapp.Models.DetailedRecipe;
import com.example.myapp.Models.Result;
import com.example.myapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeBulkListAdapter extends  RecyclerView.Adapter<RecipeListViewHolder> {
    Context context;
    List<DetailedRecipe> list;

    public RecipeBulkListAdapter(Context context, List<DetailedRecipe> list) {
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
        final DetailedRecipe recipe = list.get(position);

        holder.textViewRecipeName.setText(recipe.title);
        holder.textViewRecipeName.setSelected(true);
        Picasso.get().load(recipe.image).into(holder.imageViewRecipe);
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
