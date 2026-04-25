package com.fabricaescuela.micuenta.interfaces.rest;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fabricaescuela.micuenta.application.usecase.CreateCategoryUseCase;
import com.fabricaescuela.micuenta.application.usecase.DeleteCategoryUseCase;
import com.fabricaescuela.micuenta.application.usecase.GetCategoriesUseCase;
import com.fabricaescuela.micuenta.domain.model.Category;
import com.fabricaescuela.micuenta.domain.model.MovementType;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoriesUseCase getCategoriesUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    public CategoryController(CreateCategoryUseCase createCategoryUseCase, GetCategoriesUseCase getCategoriesUseCase, DeleteCategoryUseCase deleteCategoryUseCase) {
        this.createCategoryUseCase = createCategoryUseCase;
        this.getCategoriesUseCase = getCategoriesUseCase;
        this.deleteCategoryUseCase = deleteCategoryUseCase;
    }

    @PostMapping
    public Category create(@RequestBody Category request, Authentication auth) {
        String email = (String) auth.getPrincipal();
        return createCategoryUseCase.execute(email, request.name(), request.type());
    }

    @GetMapping
    public List<Category> list(
            @RequestParam MovementType type,
            Authentication auth
    ) {
        String email = (String) auth.getPrincipal();
        return getCategoriesUseCase.execute(email, type);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        deleteCategoryUseCase.execute(id);
    }
}