package ru.practicum.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentException(MethodArgumentNotValidException exception) {
        FieldError er = exception.getBindingResult().getFieldError();
        assert er != null;
        ErrorResponse error = ErrorResponse.builder()
                .status("BAD_REQUEST")
                .reason("Incorrectly made request.")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> HttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        ErrorResponse error = ErrorResponse.builder()
                .status("BAD_REQUEST")
                .reason("Incorrectly made request.")
                .message("incorrect date format.")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(GatewayException.class)
    public ResponseEntity<ErrorResponse> gatewayException(GatewayException e) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ErrorResponse serverError = objectMapper.readValue(e.getMessage(), ErrorResponse.class);
        return ResponseEntity.status(e.getStatus()).body(serverError);
    }
}
