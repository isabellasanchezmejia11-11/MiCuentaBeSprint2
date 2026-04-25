package com.fabricaescuela.micuenta.application.usecase;

import com.fabricaescuela.micuenta.domain.model.User;
import org.springframework.stereotype.Service;

import com.fabricaescuela.micuenta.domain.model.Category;
import com.fabricaescuela.micuenta.domain.model.MovementType;
import com.fabricaescuela.micuenta.domain.repository.CategoryRepository;
import com.fabricaescuela.micuenta.domain.repository.UserRepository;

@Service
public class CreateCategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CreateCategoryUseCase(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public Category execute(String email, String name, MovementType type) {

        User user = userRepository.findByEmail(email).orElseThrow();

        if (categoryRepository.existsByNameAndUserId(name, user.id())) {
            throw new IllegalArgumentException("Ya existe esa categoría");
        }

        return categoryRepository.save(new Category(null, name, type, user.id()));
    }
}