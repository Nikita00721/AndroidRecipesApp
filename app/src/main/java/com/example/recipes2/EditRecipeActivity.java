package com.example.recipes2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.File;

public class EditRecipeActivity extends AppCompatActivity {
    private ImageView recipeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        // Получаем данные рецепта для редактирования
        Intent intent = getIntent();
        Recipe selectedRecipe = intent.getParcelableExtra("selectedRecipe");


        recipeImageView = findViewById(R.id.recipeImageView);

        // Находим EditText для редактирования каждого поля рецепта
        if (selectedRecipe.getImagePath() != null && !selectedRecipe.getImagePath().isEmpty()) {
            File imageFile = new File(selectedRecipe.getImagePath());
            Picasso.get().load(imageFile).into(recipeImageView);
        }

        EditText editTitle = findViewById(R.id.editTitle);
        EditText editDescription = findViewById(R.id.editDescription);
        EditText editIngredients = findViewById(R.id.editIngredients);
        EditText editInstructions = findViewById(R.id.editInstructions);

        // Отображаем данные рецепта в полях для редактирования
        editTitle.setText(selectedRecipe.getTitle());
        editDescription.setText(selectedRecipe.getDescription());
        editIngredients.setText(selectedRecipe.getIngredients());
        editInstructions.setText(selectedRecipe.getInstructions());

        // Настраиваем кнопку "Сохранить изменения"
        Button saveChangesButton = findViewById(R.id.saveChangesButton);
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Получаем отредактированные данные из полей
                String editedTitle = editTitle.getText().toString();
                String editedDescription = editDescription.getText().toString();
                String editedIngredients = editIngredients.getText().toString();
                String editedInstructions = editInstructions.getText().toString();

                // Обновляем данные в объекте selectedRecipe
                selectedRecipe.setTitle(editedTitle);
                selectedRecipe.setDescription(editedDescription);
                selectedRecipe.setIngredients(editedIngredients);
                selectedRecipe.setInstructions(editedInstructions);

                // Сохраняем изменения
                RecipeDatabaseHelper databaseHelper = new RecipeDatabaseHelper(EditRecipeActivity.this);
                databaseHelper.updateRecipe(selectedRecipe);

                Intent resultIntent = new Intent();
                resultIntent.putExtra("editedRecipe", selectedRecipe);
                setResult(RESULT_OK, resultIntent);
                finish();


                finish();
            }
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Закрыть текущую активность и вернуться назад
            }
        });
    }
}
