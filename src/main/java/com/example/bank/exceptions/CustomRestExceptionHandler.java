package com.example.bank.exceptions;

import com.example.bank.dto.ErrorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;

@ControllerAdvice
@RequiredArgsConstructor
public class CustomRestExceptionHandler {

    private static final String VALIDATION_FAILED = "Validation failed";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ErrorDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        var errorDetails = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        ErrorDto errorDto = new ErrorDto(VALIDATION_FAILED, errorDetails);
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<Object> handleOtherExceptions(
            HttpMessageNotReadableException ex, WebRequest request) {
        ErrorDto errorDto = new ErrorDto(VALIDATION_FAILED, Collections.singletonList(ex.getMessage()));
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(ServiceValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ErrorDto> handleValidationExceptions(ServiceValidationException ex) {
        ErrorDto errorDto = new ErrorDto(VALIDATION_FAILED, Collections.singletonList(ex.getMessage()));
        return ResponseEntity.badRequest().body(errorDto);
    }
}
