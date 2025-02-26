package ru.petproject.ecommerce.productService.exceptions;

public class UserNotAuthException extends RuntimeException {
    public UserNotAuthException(String userId) {
        super("Пользователь с id " + userId + " не авторизован или не является администратором");
    }
}
