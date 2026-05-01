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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/categories")
@Tag(name = "Categorías", description = "Endpoints para gestionar categorías de movimientos")
@SecurityRequirement(name = "bearerAuth")
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
    @Operation(summary = "Crear nueva categoría", description = "Crea una nueva categoría para el usuario autenticado")
    @ApiResponse(responseCode = "200", description = "Categoría creada exitosamente")
    @ApiResponse(responseCode = "400", description = "Categoría duplicada o datos inválidos")
    public Category create(@RequestBody Category request, Authentication auth) {
        String email = (String) auth.getPrincipal();
        return createCategoryUseCase.execute(email, request.name(), request.type());
    }

    @GetMapping
    @Operation(summary = "Listar categorías", description = "Obtiene las categorías del usuario filtradas por tipo")
    @ApiResponse(responseCode = "200", description = "Lista de categorías")
    public List<Category> list(
            @Parameter(description = "Tipo de movimiento (INCOME o EXPENSE)")
            @RequestParam MovementType type,
            Authentication auth
    ) {
        String email = (String) auth.getPrincipal();
        return getCategoriesUseCase.execute(email, type);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría por su ID")
    @ApiResponse(responseCode = "204", description = "Categoría eliminada")
    @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    public void delete(
            @Parameter(description = "ID de la categoría")
            @PathVariable Long id
    ) {
        deleteCategoryUseCase.execute(id);
    }
}