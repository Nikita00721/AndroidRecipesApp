package com.example.recipes2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.File;

public class RecipeDetailActivity extends AppCompatActivity {
    private Recipe selectedRecipe;
    private ImageView recipeImageView;
    private TextView titleTextView, descriptionTextView, ingredientsTextView, instructionsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // Уберите объявление "Recipe" перед "selectedRecipe"
        selectedRecipe = getIntent().getParcelableExtra("selectedRecipe");

        if (selectedRecipe != null) {
            // В этом месте вы можете быть уверены, что selectedRecipe - объект типа Recipe
            // Вы можете использовать его безопасно
            Log.d("RecipeDetailActivity", "Recipe Title: " + selectedRecipe.getTitle());
            Log.d("RecipeDetailActivity", "Recipe Description: " + selectedRecipe.getDescription());
            // Остальной код
        } else {
            // Обработка ситуации, когда selectedRecipe равен null
            Log.d("RecipeDetailActivity", "selectedRecipe is null");
        }

        // Остальной код Activity
        recipeImageView = findViewById(R.id.recipeImageView);
        titleTextView = findViewById(R.id.titleTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        ingredientsTextView = findViewById(R.id.ingredientsTextView);
        instructionsTextView = findViewById(R.id.instructionsTextView);

        // Отображаем данные о рецепте
        if (selectedRecipe != null) {
            Log.d("RecipeDetailActivity", "Recipe Title: " + selectedRecipe.getTitle());
            Log.d("RecipeDetailActivity", "Recipe Description: " + selectedRecipe.getDescription());
            Log.d("RecipeDetailActivity", "Recipe Ingredients: " + selectedRecipe.getIngredients());
            Log.d("RecipeDetailActivity", "Recipe Instructions: " + selectedRecipe.getInstructions());
            titleTextView.setText(selectedRecipe.getTitle());
            descriptionTextView.setText(selectedRecipe.getDescription());
            ingredientsTextView.setText(selectedRecipe.getIngredients());
            instructionsTextView.setText(selectedRecipe.getInstructions());

            if (selectedRecipe.getImagePath() != null && !selectedRecipe.getImagePath().isEmpty()) {
                File imageFile = new File(selectedRecipe.getImagePath());
                Picasso.get().load(imageFile).into(recipeImageView);
            }
        }

        Button deleteButton = findViewById(R.id.deleteRecipeButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Удаление рецепта из базы данных
                deleteRecipeFromDatabase(selectedRecipe.getId());
                finish();
            }
        });
    }
    private void deleteRecipeFromDatabase(long recipeId) {
        RecipeDatabaseHelper databaseHelper = new RecipeDatabaseHelper(this);

        databaseHelper.deleteRecipe(recipeId);
    }
}
