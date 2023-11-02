package com.example.recipes2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecipeDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "recipes.db";
    private static final int DATABASE_VERSION = 1;

    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_RECIPES_TABLE = "CREATE TABLE " +
                RecipeContract.RecipeEntry.TABLE_NAME + " (" +
                RecipeContract.RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                RecipeContract.RecipeEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                RecipeContract.RecipeEntry.COLUMN_DESCRIPTION + " TEXT, " +
                RecipeContract.RecipeEntry.COLUMN_INGREDIENTS + " TEXT, " +
                RecipeContract.RecipeEntry.COLUMN_INSTRUCTIONS + " TEXT, " +
                RecipeContract.RecipeEntry.COLUMN_AUTHOR_ID + " INTEGER, " +
                RecipeContract.RecipeEntry.COLUMN_IMAGE_PATH + " TEXT);";

        final String SQL_CREATE_USERS_TABLE = "CREATE TABLE " +
                UserContract.UserEntry.TABLE_NAME + " (" +
                UserContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                UserContract.UserEntry.COLUMN_USERNAME + " TEXT NOT NULL, " +
                UserContract.UserEntry.COLUMN_PASSWORD + " TEXT NOT NULL, " +
                UserContract.UserEntry.COLUMN_EMAIL + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_RECIPES_TABLE);
        db.execSQL(SQL_CREATE_USERS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Обновление базы данных при необходимости
    }
}
