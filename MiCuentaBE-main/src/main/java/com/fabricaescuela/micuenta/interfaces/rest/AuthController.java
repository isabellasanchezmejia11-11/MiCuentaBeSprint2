package com.fabricaescuela.micuenta.interfaces.rest;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fabricaescuela.micuenta.application.dto.request.LoginRequest;
import com.fabricaescuela.micuenta.application.dto.request.RegisterRequest;
import com.fabricaescuela.micuenta.application.dto.response.AuthResponse;
import com.fabricaescuela.micuenta.application.dto.response.UserResponse;
import com.fabricaescuela.micuenta.application.usecase.LoginUserUseCase;
import com.fabricaescuela.micuenta.application.usecase.RegisterUserUseCase;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;

    public AuthController(
            RegisterUserUseCase registerUserUseCase,
            LoginUserUseCase loginUserUseCase
    ) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse response = registerUserUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = loginUserUseCase.execute(request);
        return ResponseEntity.ok(response);
    }
}