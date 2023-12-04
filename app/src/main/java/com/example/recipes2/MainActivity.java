package com.example.recipes2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

        // Обработчик нажатия на элемент списка
        recipeRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recipeRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Recipe selectedRecipe = recipeAdapter.getRecipeAtPosition(position);
                Log.d("СЮДА СМОТРИ", selectedRecipe.getTitle());
                Log.d("СЮДА СМОТРИ", selectedRecipe.getDescription());
                Log.d("re", "ТУТ НАЗВАНИЕ ИЗ МАЙН АКТИВИТИ" + selectedRecipe.getTitle());
                if (selectedRecipe != null) {
                    Intent intent = new Intent(MainActivity.this, RecipeDetailActivity.class);
                    intent.putExtra("selectedRecipe", selectedRecipe);
                    startActivity(intent);
                }
            }
        }));


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
        return databaseHelper.getRecipes();
    }

}
