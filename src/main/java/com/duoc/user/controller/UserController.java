package com.duoc.user.controller;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duoc.user.dto.UserResponse;
import com.duoc.user.service.UserService;
import com.duoc.user.dto.UserUpdateRequest;
import com.duoc.user.dto.UserCreateRequest;

@RestController
@RequestMapping ("/api/users")
public class UserController {
    private final UserService service;
    public UserController(UserService service) {
        this.service=service;
    }

    @GetMapping
    public List<UserResponse> list() {
        return service.listUsers();
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable("id") Long id) {
        return service.find(id);
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createUser(request));
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable("id") Long id, @Valid @RequestBody UserUpdateRequest request) {
        return service.updateUser(id,request);
    }
    
    @DeleteMapping ("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    

    
}
