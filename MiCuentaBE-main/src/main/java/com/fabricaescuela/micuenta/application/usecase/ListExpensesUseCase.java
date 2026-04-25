package com.fabricaescuela.micuenta.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fabricaescuela.micuenta.application.dto.response.MovementResponse;
import com.fabricaescuela.micuenta.application.exception.ResourceNotFoundException;
import com.fabricaescuela.micuenta.domain.model.Category;
import com.fabricaescuela.micuenta.domain.model.Movement;
import com.fabricaescuela.micuenta.domain.model.MovementType;
import com.fabricaescuela.micuenta.domain.model.User;
import com.fabricaescuela.micuenta.domain.repository.CategoryRepository;
import com.fabricaescuela.micuenta.domain.repository.MovementRepository;
import com.fabricaescuela.micuenta.domain.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ListExpensesUseCase {

    private final MovementRepository movementRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public ListExpensesUseCase(
            MovementRepository movementRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository
    ) {
        this.movementRepository = movementRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<MovementResponse> execute(String authenticatedEmail) {

        User user = userRepository.findByEmail(authenticatedEmail.trim().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        return movementRepository.findByUserIdAndType(user.id(), MovementType.EXPENSE)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private MovementResponse toResponse(Movement movement) {
        return new MovementResponse(
                movement.id(),
                movement.amount(),
                movement.date(),
                movement.type(),
                categoryRepository.findById(movement.categoryId())
                        .map(Category::name)
                        .orElse("Sin categoría"),
                movement.description()
        );
    }
}