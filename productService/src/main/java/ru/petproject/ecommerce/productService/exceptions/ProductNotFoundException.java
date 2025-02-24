package ru.petproject.ecommerce.productService.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Товар с id " + id + " не найден");
    }
}
