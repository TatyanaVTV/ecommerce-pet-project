package ru.petproject.ecommerce.productService.exceptions;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long id) {
        super("Категория с id " + id + " не найдена");
    }
}
