package com.example.recipes2;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.recipes2.R;
import com.example.recipes2.Recipe;
import com.example.recipes2.RecipeDatabaseHelper;

public class AddRecipeActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText titleEditText, descriptionEditText, ingredientsEditText, instructionsEditText;
    private ImageView recipeImageView;
    private Button attachImageButton, addRecipeButton;
    private Uri selectedImageUri;
    private RecipeDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        ingredientsEditText = findViewById(R.id.ingredientsEditText);
        instructionsEditText = findViewById(R.id.instructionsEditText);
        recipeImageView = findViewById(R.id.recipeImageView);
        attachImageButton = findViewById(R.id.attachImageButton);
        addRecipeButton = findViewById(R.id.addRecipeButton);

        databaseHelper = new RecipeDatabaseHelper(this);


        attachImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });

        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                String ingredients = ingredientsEditText.getText().toString();
                String instructions = instructionsEditText.getText().toString();

                Recipe newRecipe = new Recipe(0, title, description, ingredients, instructions, 0, "");

                long recipeId = databaseHelper.insertRecipe(newRecipe);

                if (recipeId > 0) {
                    if (selectedImageUri != null) {
                        // Сохраняем путь к изображению в базе данных и обновляем рецепт
                        String imagePath = saveImageToDatabase(selectedImageUri, recipeId);
                        if (imagePath != null) {
                            newRecipe.setImagePath(imagePath);
                            databaseHelper.updateRecipe(newRecipe);
                        }
                    }

                    finish();
                } else {
                    // Обработка ошибки
                }
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            recipeImageView.setImageURI(selectedImageUri);
        }

    }

    public String saveImageToDatabase(Uri imageUri, long recipeId) {
        RecipeDatabaseHelper databaseHelper = new RecipeDatabaseHelper(this);

        // Преобразуйте Uri изображения в путь, передав ContentResolver и Uri
        String imagePath = databaseHelper.getRealPathFromUri(getContentResolver(), imageUri);

        if (imagePath != null) {
            // Сохраните путь к изображению в базе данных, связав его с ID рецепта
            imagePath = databaseHelper.saveImageToDatabase(imageUri, recipeId);

            return imagePath;
        }

        return null;
    }

}
