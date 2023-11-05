package com.example.recipes2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipes2.R;
import com.squareup.picasso.Picasso;

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

        // Установите заголовок и описание для карточки рецепта из объекта Recipe
        holder.recipeTitleTextView.setText(recipe.getTitle());
        holder.recipeDescriptionTextView.setText(recipe.getDescription()); // Убедитесь, что описание устанавливается правильно

// Загрузите изображение рецепта с помощью Picasso (если путь к изображению доступен)
        if (recipe.getImagePath() != null && !recipe.getImagePath().isEmpty()) {
            Picasso.get().load(recipe.getImagePath()).into(holder.recipeImageView);
        } else {
            // Если путь к изображению отсутствует, установите изображение по умолчанию
            holder.recipeImageView.setImageResource(R.drawable.default_recipe_image);
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
            recipeDescriptionTextView = itemView.findViewById(R.id.recipeDescriptionTextView); // Убедитесь, что переменная правильно связана
        }
    }


    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public void updateRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }


}
