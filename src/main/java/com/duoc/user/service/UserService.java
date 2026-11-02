package com.duoc.user.service;

import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.duoc.user.dto.UserCreateRequest;
import com.duoc.user.dto.UserResponse;
import com.duoc.user.dto.UserUpdateRequest;
import com.duoc.user.error.BusinessExceptionHandler;
import com.duoc.user.repository.UserRepository;
import com.duoc.user.model.User;
import com.duoc.user.model.Role;



@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional(readOnly=true)
    public List<UserResponse> listUsers() {
        return userRepository.findAll().stream().map(UserResponse::fromEntity).toList();
    }
    @Transactional(readOnly=true)
    public UserResponse find(Long id) {
        return UserResponse.fromEntity(getUser(id));
    }
    private User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(()->
            new BusinessExceptionHandler(HttpStatus.NOT_FOUND,"Usuario no encontrado: "+id));
    }

    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        String username=request.username().toLowerCase(Locale.ROOT);
        String email=request.email().toLowerCase(Locale.ROOT);
        if (userRepository.existsByUsername(username) || userRepository.existsByEmail(email)) {
            throw new BusinessExceptionHandler(HttpStatus.CONFLICT,"Nombre de usuario o correo ya registrado");
        }
        User createdUser=userRepository.save(new User(null,username,email,
            passwordEncoder.encode(request.password()),request.role()));
        log.info("Cuenta creada id={} rol={}",createdUser.getId(),createdUser.getRole());
        return UserResponse.fromEntity(createdUser);
    }

    @Transactional
    public UserResponse updateUser(Long id,UserUpdateRequest request) {
        User actual=getUser(id);
        String email=request.email().toLowerCase(Locale.ROOT);
        if(!actual.getEmail().equals(email) && userRepository.existsByEmail(email)) {
            throw new BusinessExceptionHandler(HttpStatus.CONFLICT,"Correo ya registrado");
        }
        if (actual.getRole()==Role.ADMIN && request.role()!=Role.ADMIN
             && userRepository.countByRole(Role.ADMIN)<=1) {
            throw new BusinessExceptionHandler(HttpStatus.CONFLICT,"No se puede quitar el ultimo ADMIN");
        }
        actual.setEmail(email);
        actual.setRole(request.role());
        if(request.password()!=null) actual.setPassword(passwordEncoder.encode(request.password()));
        User saved=userRepository.save(actual);
        log.info("Cuenta actualizada id={}",id);
        return UserResponse.fromEntity(saved);
    }

    @Transactional
    public void delete(Long id) {
        User actual=getUser(id);
        if(actual.getRole()==Role.ADMIN && userRepository.countByRole(Role.ADMIN)<=1) {
            throw new BusinessExceptionHandler(HttpStatus.CONFLICT,"No se puede eliminar el ultimo ADMIN");
        }
        userRepository.delete(actual);
        log.info("Cuenta eliminada id={}",id);
    }

}
