package com.duoc.user.error;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler (BusinessExceptionHandler.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessExceptionHandler ex, HttpServletRequest req) {
        return response(ex.getStatus(),ex.getMessage(),req,Map.of());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validate(MethodArgumentNotValidException e,
                                                     HttpServletRequest r) {
        Map<String,String> errors=new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(f ->
            errors.putIfAbsent(f.getField(),f.getDefaultMessage()));
        return response(HttpStatus.BAD_REQUEST,"Solicitud invalida",r,errors);
    }
    private ResponseEntity<ErrorResponse> response(HttpStatus status,String msj,
                              HttpServletRequest r,Map<String,String> campos) {
        return ResponseEntity.status(status).body(new ErrorResponse(
            Instant.now(),status.value(),msj,r.getRequestURI(),campos));
    }
    
}
