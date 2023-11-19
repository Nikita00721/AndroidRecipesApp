package com.example.recipes2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditRecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        // Получаем данные рецепта для редактирования
        Intent intent = getIntent();
        Recipe selectedRecipe = intent.getParcelableExtra("selectedRecipe");

        // Находим EditText для редактирования каждого поля рецепта
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

                // Сохраняем изменения в базе данных (пример)
                RecipeDatabaseHelper databaseHelper = new RecipeDatabaseHelper(EditRecipeActivity.this);
                databaseHelper.updateRecipe(selectedRecipe);

                Intent resultIntent = new Intent();
                resultIntent.putExtra("editedRecipe", selectedRecipe);
                setResult(RESULT_OK, resultIntent);
                finish();

                // Закрываем активность редактирования и возвращаемся на страницу деталей рецепта
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
