package com.example.recipes2;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.recipes2.RecipeAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class RecipeDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "recipes.db";
    private static final int DATABASE_VERSION = 6;
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
                COLUMN_IMAGE_PATH + " TEXT" + // Столбец для пути к изображению
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
        values.put(COLUMN_IMAGE_PATH, recipe.getImagePath()); // Сохраняем фактический путь к изображению

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
                String imagePath = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_PATH)); // Получаем путь к изображению

                Recipe recipe = new Recipe(id, title, description, ingredients, instructions, authorId, imagePath);
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

        // Добавьте отладочный вывод, чтобы проверить, что путь к изображению обновляется
        Log.d("RecipeDatabaseHelper", "Image path updated for recipe ID: " + recipe.getId());
    }


    private String copyImageToAppDirectory(Uri imageUri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            File appDirectory = context.getFilesDir();
            String temporaryImagePath = appDirectory + File.separator + "temp_image.jpg";
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

    public String saveImageToDatabase(Uri imageUri, long recipeId) {
        String temporaryImagePath = copyImageToAppDirectory(imageUri);
        if (temporaryImagePath != null) {
            // Создайте уникальное имя файла на основе recipeId
            String uniqueImagePath = "recipe_image_" + recipeId + ".jpg";
            File appDirectory = context.getFilesDir();
            File destinationFile = new File(appDirectory, uniqueImagePath);

            if (copyFile(new File(temporaryImagePath), destinationFile)) {
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(COLUMN_IMAGE_PATH, destinationFile.getAbsolutePath());

                int rowsUpdated = db.update(TABLE_RECIPES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(recipeId)});
                db.close();

                if (rowsUpdated > 0) {
                    return destinationFile.getAbsolutePath();
                }
            }
        }

        return null;
    }


    private boolean copyFile(File source, File destination) {
        try {
            FileInputStream inStream = new FileInputStream(source);
            FileOutputStream outStream = new FileOutputStream(destination);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
            inStream.close();
            outStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }




    public String getRealPathFromUri(ContentResolver contentResolver, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = contentResolver.query(uri, projection, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String path = "file://" + cursor.getString(columnIndex); // Добавьте "file://" перед путем к файлу
            cursor.close();
            return path;
        }

        return null;
    }

    public void deleteRecipe(long recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("recipes", "_id=?", new String[]{String.valueOf(recipeId)});
        db.close();
    }
}
