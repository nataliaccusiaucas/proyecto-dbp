package com.hirehub.backend.common;

import com.hirehub.backend.common.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, Object> error = Map.of(
                "timestamp", LocalDateTime.now(),
                "error", "Recurso no encontrado",
                "details", ex.getMessage(),
                "status", 404
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateResource(DuplicateResourceException ex) {
        Map<String, Object> error = Map.of(
                "timestamp", LocalDateTime.now(),
                "error", "Conflicto de recursos",
                "details", ex.getMessage(),
                "status", 409
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidOperation(InvalidOperationException ex) {
        Map<String, Object> error = Map.of(
                "timestamp", LocalDateTime.now(),
                "error", "Operación inválida",
                "details", ex.getMessage(),
                "status", 400
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorized(UnauthorizedException ex) {
        Map<String, Object> error = Map.of(
                "timestamp", LocalDateTime.now(),
                "error", "No autorizado",
                "details", ex.getMessage(),
                "status", 401
        );
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(ValidationException ex) {
        Map<String, Object> error = Map.of(
                "timestamp", LocalDateTime.now(),
                "error", "Validación fallida",
                "details", ex.getMessage(),
                "status", 400
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, Object>> handleConflict(ConflictException ex) {
        Map<String, Object> error = Map.of(
                "timestamp", LocalDateTime.now(),
                "error", "Conflicto",
                "details", ex.getMessage(),
                "status", 409
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<Map<String, Object>> handleInternalServer(InternalServerException ex) {
        Map<String, Object> error = Map.of(
                "timestamp", LocalDateTime.now(),
                "error", "Error interno del servidor",
                "details", ex.getMessage(),
                "status", 500
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, Object> error = Map.of(
                "timestamp", LocalDateTime.now(),
                "error", "Errores de validación",
                "details", ex.getBindingResult().getFieldErrors().stream()
                        .map(err -> err.getField() + ": " + err.getDefaultMessage())
                        .collect(Collectors.toList()),
                "status", 400
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException() {
        Map<String, Object> error = Map.of(
                "timestamp", LocalDateTime.now(),
                "error", "Error interno",
                "details", "Ocurrió un error inesperado",
                "status", 500
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
