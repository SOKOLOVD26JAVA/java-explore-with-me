package ru.practicum.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class GatewayException extends RuntimeException {
    private final HttpStatus status;

    public GatewayException(HttpStatus status, String message) {
        super(message);
        this.status = status;

    }
}
