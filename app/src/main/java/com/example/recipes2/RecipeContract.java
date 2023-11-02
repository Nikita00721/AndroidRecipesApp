package com.example.recipes2;

import android.provider.BaseColumns;

public class RecipeContract {
    public static final class RecipeEntry implements BaseColumns {
        public static final String TABLE_NAME = "recipes";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_INGREDIENTS = "ingredients";
        public static final String COLUMN_INSTRUCTIONS = "instructions";
        public static final String COLUMN_AUTHOR_ID = "author_id";
        public static final String COLUMN_IMAGE_PATH = "image_path";

    }
}
