package org.jnu.unimart.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String defaultCategoryNotFound) {
        super(defaultCategoryNotFound);
    }
}
