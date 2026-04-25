package com.fabricaescuela.micuenta.application.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fabricaescuela.micuenta.application.dto.request.RegisterRequest;
import com.fabricaescuela.micuenta.application.dto.response.UserResponse;
import com.fabricaescuela.micuenta.application.exception.EmailAlreadyExistsException;
import com.fabricaescuela.micuenta.application.port.out.PasswordHasher;
import com.fabricaescuela.micuenta.domain.model.User;
import com.fabricaescuela.micuenta.domain.repository.UserRepository;

@Service
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public RegisterUserUseCase(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    @Transactional
    public UserResponse execute(RegisterRequest request) {
        String normalizedEmail = request.email().trim().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new EmailAlreadyExistsException("El correo ya está registrado");
        }

        String encodedPassword = passwordHasher.encode(request.password());

        User savedUser = userRepository.save(
                new User(
                        null,
                        request.name().trim(),
                        request.lastname().trim(),
                        normalizedEmail,
                        encodedPassword
                )
        );

        return new UserResponse(savedUser.id(), savedUser.name(), savedUser.lastname(), savedUser.email());
    }
}