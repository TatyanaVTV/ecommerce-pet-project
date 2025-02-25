package ru.petproject.ecommerce.productService.exceptions;

public class UserNotAuthException extends RuntimeException {
    public UserNotAuthException(String userLog) {
        super("Пользователь с логином " + userLog + " не авторизован или не является администратором");
    }
}
