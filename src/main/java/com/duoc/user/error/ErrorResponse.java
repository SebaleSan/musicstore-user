package com.duoc.user.error;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(Instant moment, int status, String message, String path, Map<String,String> fields) { 
    
}
