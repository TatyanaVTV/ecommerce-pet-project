package ru.aston.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse {
    private int status;
    private String error;
    private String reason;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(int status, String error, String reason, String message) {
        this.status = status;
        this.error = error;
        this.reason = reason;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}