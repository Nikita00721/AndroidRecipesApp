package com.example.recipes2;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.MediaStore;
import com.example.recipes2.RecipeAdapter;
import java.util.ArrayList;
import java.util.List;

public class RecipeDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "recipes.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_RECIPES = "recipes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_INGREDIENTS = "ingredients";
    public static final String COLUMN_INSTRUCTIONS = "instructions";
    public static final String COLUMN_AUTHOR_ID = "authorId";
    public static final String COLUMN_IMAGE_RESOURCE_ID = "imageResourceId";
    public static final String COLUMN_IMAGE_PATH = "imagePath"; // Новый столбец для хранения пути к изображению
    private Context context;

    public RecipeDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context; // Сохраняем контекст
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_RECIPES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_INGREDIENTS + " TEXT, " +
                COLUMN_INSTRUCTIONS + " TEXT, " +
                COLUMN_AUTHOR_ID + " INTEGER, " +
                COLUMN_IMAGE_RESOURCE_ID + " INTEGER, " +
                COLUMN_IMAGE_PATH + " TEXT" + // Добавляем столбец для пути к изображению
                ")";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Удалить существующую таблицу
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        // Создать новую таблицу
        onCreate(db);
    }


    public long insertRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, recipe.getTitle());
        values.put(COLUMN_DESCRIPTION, recipe.getDescription());
        values.put(COLUMN_INGREDIENTS, recipe.getIngredients());
        values.put(COLUMN_INSTRUCTIONS, recipe.getInstructions());
        values.put(COLUMN_AUTHOR_ID, recipe.getAuthorId());
        values.put(COLUMN_IMAGE_PATH, recipe.getImagePath()); // Сохраняем путь к изображению

        long recipeId = db.insert(TABLE_RECIPES, null, values);
        db.close();
        return recipeId;
    }

    public List<Recipe> getRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                COLUMN_ID,
                COLUMN_TITLE,
                COLUMN_DESCRIPTION,
                COLUMN_INGREDIENTS,
                COLUMN_INSTRUCTIONS,
                COLUMN_AUTHOR_ID,
                COLUMN_IMAGE_RESOURCE_ID,
                COLUMN_IMAGE_PATH // Включаем путь к изображению
        };

        Cursor cursor = db.query(TABLE_RECIPES, columns, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                String ingredients = cursor.getString(cursor.getColumnIndex(COLUMN_INGREDIENTS));
                String instructions = cursor.getString(cursor.getColumnIndex(COLUMN_INSTRUCTIONS));
                int authorId = cursor.getInt(cursor.getColumnIndex(COLUMN_AUTHOR_ID));
                int imageResourceId = cursor.getInt(cursor.getColumnIndex(COLUMN_IMAGE_RESOURCE_ID));
                String imagePath = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_PATH)); // Получаем путь к изображению

                Recipe recipe = new Recipe(id, title, description, ingredients, instructions, authorId, String.valueOf(imageResourceId));
                recipe.setImagePath(imagePath); // Устанавливаем путь к изображению
                recipes.add(recipe);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return recipes;
    }

    public void updateRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, recipe.getTitle());
        values.put(COLUMN_DESCRIPTION, recipe.getDescription());
        values.put(COLUMN_INGREDIENTS, recipe.getIngredients());
        values.put(COLUMN_INSTRUCTIONS, recipe.getInstructions());
        values.put(COLUMN_IMAGE_PATH, recipe.getImagePath());

        db.update(TABLE_RECIPES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(recipe.getId())});
        db.close();
    }

    public String saveImageToDatabase(Uri imageUri, long recipeId) {
        // Преобразуйте Uri изображения в путь, передав ContentResolver и Uri
        String imagePath = getRealPathFromUri(context.getContentResolver(), imageUri);

        if (imagePath != null) {
            // Сохраните путь к изображению в базе данных, связав его с ID рецепта
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_IMAGE_PATH, imagePath);

            db.update(TABLE_RECIPES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(recipeId)});
            db.close();

            return imagePath;
        }

        return null;
    }



    public String getRealPathFromUri(ContentResolver contentResolver, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = contentResolver.query(uri, projection, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }

        return null;
    }


}
