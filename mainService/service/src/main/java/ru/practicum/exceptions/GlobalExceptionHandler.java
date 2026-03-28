package ru.practicum.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> dataIntegrityException(ConflictException exception) {
        ErrorResponse error = ErrorResponse.builder()
                .status("CONFLICT")
                .reason("Integrity constraint has been violated.")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundException(NotFoundException exception) {
        ErrorResponse error = ErrorResponse.builder().status("NOT_FOUND")
                .reason("The required object was not found.")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(AccessException.class)
    public ResponseEntity<ErrorResponse> accessException(AccessException exception) {
        ErrorResponse error = ErrorResponse.builder().status("FORBIDDEN")
                .reason("You don't have enough access rights.")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> badRequestException(BadRequestException exception) {
        ErrorResponse error = ErrorResponse.builder().status("BAD_REQUEST")
                .reason("Incorrectly made request.")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(BadTimeException.class)
    public ResponseEntity<ErrorResponse> badTimeException(BadTimeException exception) {
        ErrorResponse error = ErrorResponse.builder().status("BAD_REQUEST")
                .reason("For the requested operation the conditions are not met")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


}
