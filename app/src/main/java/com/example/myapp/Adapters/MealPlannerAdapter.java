package com.example.myapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.DetailedRecipeActivity;
import com.example.myapp.Models.ExtendedIngredient;
import com.example.myapp.Models.Meal;
import com.example.myapp.Models.MealPlanner;
import com.example.myapp.Models.Week;
import com.example.myapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MealPlannerAdapter extends  RecyclerView.Adapter<RecipeListViewHolder>{

    Context context;
    List<Meal> list;

    public MealPlannerAdapter(Context context, List<Meal> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public RecipeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new RecipeListViewHolder(LayoutInflater.from(context).inflate(R.layout.recipe,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListViewHolder holder, int position) {
        final Meal meal = list.get(position);
        holder.textViewRecipeName.setText(meal.title);
        holder.textViewRecipeName.setSelected(true);
        String imageUrl = "https://img.spoonacular.com/recipes/{id}-312x231.jpg";
        String replacedUrl = imageUrl.replace("{id}", Integer.toString(meal.id));

        Picasso.get().load(replacedUrl).into(holder.imageViewRecipe);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to detail recipe activity
                Intent intent = new Intent(context, DetailedRecipeActivity.class);
                intent.putExtra("recipeId", meal.id); // Pass the recipe object to detail activity
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
