package com.example.recipes2;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Recipe implements Parcelable {
    private long id;
    private String title;
    private String description;
    private String ingredients;
    private String instructions;
    private int authorId;
    private String imagePath; // Добавляем поле для хранения пути к изображению

    public Recipe(long id, String title, String description, String ingredients, String instructions, int authorId, String imagePath) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.authorId = authorId;
        this.imagePath = imagePath; // Инициализируем поле для пути к изображению
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getId() {
        return (int) id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public int getAuthorId() {
        return authorId;
    }

    public String getImagePath() {
        return imagePath; // Метод для получения пути к изображению
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath; // Метод для установки пути к изображению
    }

    protected Recipe(Parcel in) {
        id = in.readLong();
        title = in.readString();
        description = in.readString();
        ingredients = in.readString();
        instructions = in.readString();
        authorId = in.readInt();
        imagePath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(ingredients);
        dest.writeString(instructions);
        dest.writeInt(authorId);
        dest.writeString(imagePath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
