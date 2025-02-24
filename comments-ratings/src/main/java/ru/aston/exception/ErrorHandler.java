package ru.aston.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandler {

    // ❌ 400 BAD REQUEST (ошибки валидации)
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationErrors(Exception ex) {
        String message = ex instanceof MethodArgumentNotValidException ?
                ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors()
                        .stream()
                        .map(error -> error.getField() + ": " + error.getDefaultMessage())
                        .collect(Collectors.joining("; ")) :
                ex.getMessage();

        return buildResponse(HttpStatus.BAD_REQUEST, "Ошибка валидации", message);
    }

    // ❌ 403 FORBIDDEN (доступ запрещен)
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "Доступ запрещен", ex.getMessage());
    }

    // ❌ 404 NOT FOUND (объект не найден)
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Объект не найден", ex.getMessage());
    }

    // ❌ 409 CONFLICT (конфликт данных)
    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex) {
        return buildResponse(HttpStatus.CONFLICT, "Конфликт данных", ex.getMessage());
    }

    // ❌ 500 INTERNAL SERVER ERROR (непредвиденная ошибка)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleInternalServerError(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера", ex.getMessage());
    }

    // 🔧 Универсальный метод для формирования ответа
    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String reason, String message) {
        return ResponseEntity.status(status).body(new ErrorResponse(status.value(), status.getReasonPhrase(), reason, message));
    }
}

