package org.jnu.unimart.pojo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 主键自增
    @Column(name = "category_id")
    private int categoryId;

    @NotBlank
    @Column(name = "category_name")
    private String categoryName;
    @Column(name = "parent_category_id")
    private int parentId; // 父分类id

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public @NotBlank String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(@NotBlank String categoryName) {
        this.categoryName = categoryName;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", parentId=" + parentId +
                '}';
    }
}
