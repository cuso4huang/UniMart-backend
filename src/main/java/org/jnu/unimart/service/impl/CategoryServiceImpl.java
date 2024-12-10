package org.jnu.unimart.service.impl;

import jakarta.transaction.Transactional;
import org.jnu.unimart.pojo.Category;
import org.jnu.unimart.repository.CategoryRepository;
import org.jnu.unimart.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(int id) {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isPresent()){
            return category.get();
        }
        throw new IllegalArgumentException("Category not found with id:"+id);
    }

    @Override
    public Category getDefaultCategory() {
        Optional<Category> defaultCategory = categoryRepository.findByCategoryName("其他");
        if(defaultCategory.isPresent()){
            return defaultCategory.get();
        }
        throw new IllegalArgumentException("defaultCategory not found");
    }

    @Override
    public Category getCategoryByName(String name) {
        Optional<Category> byCategoryName = categoryRepository.findByCategoryName(name);
        if(byCategoryName.isPresent()){
            return byCategoryName.get();
        }
        throw new IllegalArgumentException("Category not found with name:"+name);
    }

    @Override
    public Category createCategory(Category category) {
        // 校验标签名称是否合法
        String categoryName = category.getCategoryName();
        if (categoryName.length() > 20) {
            throw new IllegalArgumentException("category name is too long. Maximum length is 20 characters.");
        }

        // 设置清理后的标签名称
        category.setCategoryName(categoryName);

        // 检查标签是否已经存在，避免重复创建
        Optional<Category> existingCategory = categoryRepository.findByCategoryName(categoryName);
        if (existingCategory.isPresent()) {
            throw new IllegalArgumentException("category with this name already exists.");
        }

        // 保存标签
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category) {
        Optional<Category> byId = categoryRepository.findById(category.getCategoryId());
        if (byId.isEmpty()) {
            throw new IllegalArgumentException("Category not found with id:"+category.getCategoryId());
        }
        if(category.getCategoryName().length() > 20 ) {
            throw new IllegalArgumentException("category name is too long. Maximum length is 20 characters.");
        }
        if(category.getCategoryName() == null || category.getCategoryName().trim().isEmpty()){
            throw new IllegalArgumentException("category name is null or empty.");
        }
        category.setCategoryName(category.getCategoryName());
        Optional<Category> parentCategory = categoryRepository.findById(category.getParentId());
        if (parentCategory.isPresent()) {
            category.setParentId(parentCategory.get().getCategoryId());
        }
        else {
            category.setParentId(0);
        }
        return categoryRepository.save(category);

    }

    @Override
    public Category setParentCategoryByName(Category category, String parentName){
        Optional<Category> parentCategory = categoryRepository.findByCategoryName(parentName);
//        System.out.println(parentCategory);
        if(parentCategory.isPresent()){
            category.setParentId(parentCategory.get().getCategoryId());
            return categoryRepository.save(category);
        }
        throw new IllegalArgumentException("Category not found with name:"+parentName);

    }
}
