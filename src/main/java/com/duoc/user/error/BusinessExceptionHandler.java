package com.duoc.user.error;
import org.springframework.http.HttpStatus;
public class BusinessExceptionHandler extends RuntimeException {
    private final HttpStatus status;

    public BusinessExceptionHandler(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
    
}
