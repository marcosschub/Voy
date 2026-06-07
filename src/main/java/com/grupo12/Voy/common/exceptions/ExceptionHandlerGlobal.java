package com.grupo12.Voy.common.exceptions;

import com.grupo12.Voy.common.models.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandlerGlobal {
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> handlerAlreadyExistsException(AlreadyExistsException ex, WebRequest request){
        ErrorDetails errorDetails = errorBuilder(ex,request);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDetails);
    }

    @ExceptionHandler(EntityDuplicatedException.class)
    public ResponseEntity<ErrorDetails> handlerEntityDuplicatedException(EntityDuplicatedException ex, WebRequest request){
        ErrorDetails errorDetails = errorBuilder(ex,request);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDetails);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDetails> handlerEntityNotFoundException(EntityNotFoundException ex, WebRequest request){
        ErrorDetails errorDetails = errorBuilder(ex,request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }

    @ExceptionHandler(ExceededAmountException.class)
    public ResponseEntity<ErrorDetails> handlerExceededAmountException(ExceededAmountException ex, WebRequest request){
        ErrorDetails errorDetails = errorBuilder(ex,request);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDetails);
    }

    @ExceptionHandler(InvalidFieldException.class)
    public ResponseEntity<ErrorDetails> handlerInvalidFieldException(InvalidFieldException ex, WebRequest request){
        ErrorDetails errorDetails = errorBuilder(ex,request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    @ExceptionHandler(NotAllowedException.class)
    public ResponseEntity<ErrorDetails> handlerNotAllowedException(NotAllowedException ex, WebRequest request){
        ErrorDetails errorDetails = errorBuilder(ex,request);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetails);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handlerValidations(MethodArgumentNotValidException ex,WebRequest request) {
        String errorList = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining());
        ErrorDetails response = new ErrorDetails(errorList,
                request.getDescription(false),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    public ErrorDetails errorBuilder(RuntimeException ex, WebRequest request){
        return ErrorDetails
                .builder()
                .message(ex.getMessage())
                .endpoint(request.getDescription(false))
                .dateTime(LocalDateTime.now())
                .build();
    }
}
