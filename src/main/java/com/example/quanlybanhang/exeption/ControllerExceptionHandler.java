package com.example.quanlybanhang.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleUncaughtException(Exception exception, WebRequest request) {
        ErrorMessage message = new ErrorMessage();
        message.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        message.setMessage(exception.getMessage());
        message.setDescription(request.getDescription(false));
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorMessage> badRequestException(BadRequestException exception, WebRequest request) {
        ErrorMessage message = new ErrorMessage();
        message.setStatusCode(HttpStatus.BAD_REQUEST.value());
        message.setMessage(exception.getMessage());
        message.setDescription(request.getDescription(false));
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}
