package org.jnu.unimart.service;

import org.jnu.unimart.pojo.Category;

import java.util.List;

public interface CategoryService {
    public List<Category> getAllCategories();
    public Category getCategoryById(int id);
    public Category getDefaultCategory();
    public Category getCategoryByName(String name);
    public Category createCategory(Category category);
    public Category updateCategory(Category category);

    Category setParentCategoryByName(Category category, String parentName);
}
