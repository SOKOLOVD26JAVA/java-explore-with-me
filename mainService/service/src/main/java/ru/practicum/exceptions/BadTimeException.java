package ru.practicum.exceptions;

public class BadTimeException extends RuntimeException {
    public BadTimeException(String message) {
        super(message);
    }
}
