package org.jnu.unimart.service.impl;

import org.jnu.unimart.pojo.Category;
import org.jnu.unimart.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CategoryServiceImplTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    void getAllCategories() {
        List<Category> allCategories = categoryService.getAllCategories();
        for (Category category : allCategories) {
            System.out.println(category);
        }
    }

    @Test
    void getCategoryById() {
        Category category = categoryService.getCategoryById(1);
        System.out.println(category);
    }

    @Test
    void getDefaultCategory() {
        Category category = categoryService.getDefaultCategory();
        System.out.println(category);
    }

    @Test
    void getCategoryByName() {
        Category 其他 = categoryService.getCategoryByName("其他");
        System.out.println(其他);
        Category ta = categoryService.getCategoryByName("ta");
        System.out.println(ta);
    }

    @Test
    void createCategory() {
        Category category = new Category();
        category.setCategoryName("其他");
//        category.setCategoryId(1);
        Category category1 = categoryService.createCategory(category);
        System.out.println(category1);
    }

    @Test
    void updateCategory() {
        Category category = new Category();
        category.setCategoryId(2);
        category.setCategoryName("手机");
        category.setParentId(3);

        Category category1 = categoryService.updateCategory(category);
        System.out.println(category1);

    }

    @Test
    void setParentCategoryByName() {
        Category category =categoryService.getCategoryById(4);
        categoryService.setParentCategoryByName(category,"其他");
        System.out.println(category);

    }
}