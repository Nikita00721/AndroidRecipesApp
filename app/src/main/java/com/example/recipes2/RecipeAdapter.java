package com.example.recipes2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private Context context;
    private List<Recipe> recipes;

    public RecipeAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_card_layout, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);

        // Устанавливаем данные в элементы макета
        holder.recipeTitleTextView.setText(recipe.getTitle());
        holder.recipeDescriptionTextView.setText(recipe.getDescription());

        if (recipe.getImagePath() != null && !recipe.getImagePath().isEmpty()) {
            File imageFile = new File(recipe.getImagePath());
            Picasso.get().load(imageFile).into(holder.recipeImageView);
        } else {
            holder.recipeImageView.setImageResource(R.drawable.default_recipe_image);
        }
    }

    public Recipe getRecipeAtPosition(int position) {
        if (position >= 0 && position < recipes.size()) {
            return recipes.get(position);
        } else {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView recipeImageView;
        TextView recipeTitleTextView;
        TextView recipeDescriptionTextView;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            recipeImageView = itemView.findViewById(R.id.recipeImageView);
            recipeTitleTextView = itemView.findViewById(R.id.recipeTitleTextView);
            recipeDescriptionTextView = itemView.findViewById(R.id.recipeDescriptionTextView);
        }
    }

    public void updateRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }


}
