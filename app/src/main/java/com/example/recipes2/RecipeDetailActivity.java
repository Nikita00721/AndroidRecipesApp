package com.example.recipes2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.io.File;

public class RecipeDetailActivity extends AppCompatActivity {
    private Recipe selectedRecipe;
    private ImageView recipeImageView;
    private TextView titleTextView, descriptionTextView, ingredientsTextView, instructionsTextView;
    private static final int EDIT_RECIPE_REQUEST_CODE = 1; // Код запроса для редактирования рецепта

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        selectedRecipe = getIntent().getParcelableExtra("selectedRecipe");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (selectedRecipe != null) {
            TextView marqueeTitle = findViewById(R.id.toolbar_title);
            marqueeTitle.setText(selectedRecipe.getTitle());
            marqueeTitle.setSelected(true); // Включить бегущую строку
            getSupportActionBar().setDisplayShowTitleEnabled(false); // Отключить обычный заголовок
        } else {
            // Обработка ситуации, когда selectedRecipe равен null
            Log.d("RecipeDetailActivity", "selectedRecipe is null");
        }

        // Находим кнопку "Редактировать рецепт" в макете
        Button editRecipeButton = findViewById(R.id.editRecipeButton);

        // Устанавливаем обработчик нажатия на кнопку
        editRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeDetailActivity.this, EditRecipeActivity.class);
                intent.putExtra("selectedRecipe", selectedRecipe);
                startActivityForResult(intent, EDIT_RECIPE_REQUEST_CODE);
            }
        });


        recipeImageView = findViewById(R.id.recipeImageView);
        titleTextView = findViewById(R.id.titleTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        ingredientsTextView = findViewById(R.id.ingredientsTextView);
        instructionsTextView = findViewById(R.id.instructionsTextView);

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
                // Создаем диалоговое окно подтверждения удаления
                new AlertDialog.Builder(RecipeDetailActivity.this)
                        .setTitle("Подтверждение")
                        .setMessage("Вы уверены, что хотите удалить этот рецепт?")
                        .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Удаление рецепта из базы данных
                                deleteRecipeFromDatabase(selectedRecipe.getId());
                                finish();
                            }
                        })
                        .setNegativeButton("Отмена", null)
                        .show();
            }
        });



    }
    private void deleteRecipeFromDatabase(long recipeId) {
        RecipeDatabaseHelper databaseHelper = new RecipeDatabaseHelper(this);

        databaseHelper.deleteRecipe(recipeId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_RECIPE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Recipe editedRecipe = data.getParcelableExtra("editedRecipe");
            if (editedRecipe != null) {
                // Обновляем данные рецепта
                selectedRecipe = editedRecipe;
                // Обновляем отображение
                updateRecipeDetails();
            }
        }
    }

    private void updateRecipeDetails() {
        titleTextView.setText(selectedRecipe.getTitle());
        descriptionTextView.setText(selectedRecipe.getDescription());
        ingredientsTextView.setText(selectedRecipe.getIngredients());
        instructionsTextView.setText(selectedRecipe.getInstructions());

        if (selectedRecipe.getImagePath() != null && !selectedRecipe.getImagePath().isEmpty()) {
            File imageFile = new File(selectedRecipe.getImagePath());
            Picasso.get().load(imageFile).into(recipeImageView);
        }
    }

}
