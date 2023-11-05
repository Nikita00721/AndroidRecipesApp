package com.example.recipes2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipes2.RecipeDatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecipeDatabaseHelper databaseHelper;
    private RecipeAdapter recipeAdapter;
    private FloatingActionButton addRecipeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация базы данных
        databaseHelper = new RecipeDatabaseHelper(this);

        // Инициализация RecyclerView
        RecyclerView recipeRecyclerView = findViewById(R.id.recipeRecyclerView);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeAdapter = new RecipeAdapter(this, getRecipesFromDatabase());
        recipeRecyclerView.setAdapter(recipeAdapter);

        // Инициализация кнопки "Добавить рецепт"
        addRecipeButton = findViewById(R.id.addRecipeButton);
        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Переход на активность для добавления нового рецепта
                Intent intent = new Intent(MainActivity.this, AddRecipeActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Обновить список рецептов из базы данных
        List<Recipe> recipes = getRecipesFromDatabase();

        // Обновить данные в адаптере и уведомить его об изменениях
        recipeAdapter.updateRecipes(recipes);
        recipeAdapter.notifyDataSetChanged();
    }


    private List<Recipe> getRecipesFromDatabase() {
        // Здесь вы должны выполнить запрос к базе данных SQLite и вернуть список рецептов
        // Например, вы можете использовать SQLiteDatabase или Room для работы с базой данных.
        // В данном примере используется фиктивный метод.
        return databaseHelper.getRecipes();
    }

}
