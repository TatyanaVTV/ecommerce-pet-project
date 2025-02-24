package ru.petproject.ecommerce.order_service.exception;

public class WrongNumberArgException extends RuntimeException {
    public WrongNumberArgException(String message) {
        super(message);
    }
}
