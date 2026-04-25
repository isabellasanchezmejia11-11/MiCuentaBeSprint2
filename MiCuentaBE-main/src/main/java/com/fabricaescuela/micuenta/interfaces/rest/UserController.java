package com.fabricaescuela.micuenta.interfaces.rest;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fabricaescuela.micuenta.application.dto.response.UserResponse;
import com.fabricaescuela.micuenta.application.usecase.GetCurrentUserUseCase;

@RestController
@RequestMapping("/users")
public class UserController {

    private final GetCurrentUserUseCase getCurrentUserUseCase;

    public UserController(GetCurrentUserUseCase getCurrentUserUseCase) {
        this.getCurrentUserUseCase = getCurrentUserUseCase;
    }

    @GetMapping("/me")
    public UserResponse me(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        return getCurrentUserUseCase.execute(email);
    }
}