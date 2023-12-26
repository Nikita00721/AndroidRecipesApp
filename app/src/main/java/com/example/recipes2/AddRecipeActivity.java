package com.example.recipes2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AddRecipeActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText titleEditText, descriptionEditText, ingredientsEditText, instructionsEditText;
    private ImageView recipeImageView;
    private Button attachImageButton, addRecipeButton;
    private Uri selectedImageUri;
    private RecipeDatabaseHelper databaseHelper;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

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

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            selectedImageUri = result.getData().getData();
                            recipeImageView.setImageURI(selectedImageUri);
                        }
                    }
                }
        );

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

                boolean fieldsAreEmpty = title.trim().isEmpty() || description.trim().isEmpty() || ingredients.trim().isEmpty() || instructions.trim().isEmpty();

                if (fieldsAreEmpty) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddRecipeActivity.this);
                    builder.setMessage("Вы точно хотите создать рецепт без заполнения всех полей?")
                            .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Продолжить добавление рецепта
                                    addRecipe();
                                }
                            })
                            .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Отмена операции
                                    dialog.dismiss();
                                }
                            });
                    // Создание и отображение диалогового окна
                    builder.create().show();
                } else {
                    // Все поля заполнены, добавление рецепта
                    addRecipe();
                }
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    public String saveImageToDatabase(Uri imageUri, long recipeId) {
        String temporaryImagePath = copyImageToAppDirectory(imageUri, recipeId);
        if (temporaryImagePath != null) {
            databaseHelper.saveImageToDatabase(Uri.parse(temporaryImagePath), recipeId);
            return temporaryImagePath;
        }
        return null;
    }


    public String copyImageToAppDirectory(Uri imageUri, long recipeId) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            File appDirectory = getFilesDir(); // Каталог вашего приложения
            String temporaryImagePath = appDirectory + File.separator + "recipe_image_" + recipeId + ".jpg";
            OutputStream outputStream = new FileOutputStream(temporaryImagePath);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();
            return temporaryImagePath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Метод для добавления рецепта
    private void addRecipe() {
        String title = titleEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String ingredients = ingredientsEditText.getText().toString();
        String instructions = instructionsEditText.getText().toString();
        // Оставшаяся часть кода для добавления рецепта без проверки пустых полей
        Recipe newRecipe = new Recipe(0, title, description, ingredients, instructions, 0, "");
        long recipeId = databaseHelper.insertRecipe(newRecipe);

        if (recipeId > 0) {
            newRecipe.setId(recipeId);
            if (selectedImageUri != null) {
                String imagePath = saveImageToDatabase(selectedImageUri, recipeId);
                if (imagePath != null) {
                    newRecipe.setImagePath(imagePath);
                }
            }

            databaseHelper.updateRecipe(newRecipe);

            finish();
        } else {
            // Обработка ошибки
        }
    }


}
