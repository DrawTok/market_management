package com.example.market_management.Services;

import com.example.market_management.Models.Category;
import com.example.market_management.Models.Department;

import java.util.List;

public class CategorySingleton {
    private static CategorySingleton instance;

    private List<Category> categories;

    private CategorySingleton() {}

    public static CategorySingleton getInstance() {
        if (instance == null) {
            synchronized (CategorySingleton.class) {
                if (instance == null) {
                    instance = new CategorySingleton();
                }
            }
        }
        return instance;
    }

    public void setCategories(List<Category> list) {
        this.categories = list;
    }

    public List<Category> getCategories() {
        return categories;
    }
}
