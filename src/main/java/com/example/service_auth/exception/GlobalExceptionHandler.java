package com.example.service_auth.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errores);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String mensaje = "Error de integridad en la base de datos";

        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException cve) {
            String constraintName = cve.getConstraintName();
            if (constraintName != null && constraintName.contains("correo")) {
                mensaje = "El correo ya está registrado en el sistema";
            }
        }

        return ResponseEntity.badRequest().body(Map.of("error", mensaje));
    }
}
