package com.example.recipes2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.File;

public class EditRecipeActivity extends AppCompatActivity {
    private ImageView recipeImageView;
    private Button attachImageButton;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Uri selectedImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);
        attachImageButton = findViewById(R.id.attachImageButton);
        // Получаем данные рецепта для редактирования
        Intent intent = getIntent();
        Recipe selectedRecipe = intent.getParcelableExtra("selectedRecipe");

        recipeImageView = findViewById(R.id.recipeImageView);
        if (selectedRecipe.getImagePath() != null && !selectedRecipe.getImagePath().isEmpty()) {
            File imageFile = new File(selectedRecipe.getImagePath());
            Picasso.get().load(imageFile).into(recipeImageView);
        }

        attachImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            selectedImageUri = result.getData().getData();
                            // Обновляем изображение на экране после выбора нового изображения
                            recipeImageView.setImageURI(selectedImageUri);
                        }
                    }
                }
        );

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

                RecipeDatabaseHelper databaseHelper = new RecipeDatabaseHelper(EditRecipeActivity.this);
                if (selectedImageUri != null) {
                    String imagePath = databaseHelper.saveImageToDatabase(selectedImageUri, selectedRecipe.getId());
                    if (imagePath != null) {
                        selectedRecipe.setImagePath(imagePath);
                    }
                }
                databaseHelper.updateRecipe(selectedRecipe);

                Intent resultIntent = new Intent();
                resultIntent.putExtra("editedRecipe", selectedRecipe);
                setResult(RESULT_OK, resultIntent);
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
    public void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }
}
