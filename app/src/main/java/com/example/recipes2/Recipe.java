package com.example.recipes2;

public class Recipe {
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
}
