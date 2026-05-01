package com.fabricaescuela.micuenta.interfaces.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fabricaescuela.micuenta.application.dto.request.CreateBudgetRequest;
import com.fabricaescuela.micuenta.application.dto.response.BudgetResponse;
import com.fabricaescuela.micuenta.application.usecase.CreateBudgetUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/budgets")
@Tag(name = "Presupuestos", description = "Endpoints para gestionar presupuestos")
@SecurityRequirement(name = "bearerAuth")
public class BudgetController {

    private final CreateBudgetUseCase createBudgetUseCase;

    public BudgetController(CreateBudgetUseCase createBudgetUseCase) {
        this.createBudgetUseCase = createBudgetUseCase;
    }

    @PostMapping
    @Operation(summary = "Crear presupuesto", description = "Crea un nuevo presupuesto para una categoría")
    @ApiResponse(responseCode = "201", description = "Presupuesto creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o presupuesto duplicado")
    public ResponseEntity<BudgetResponse> create(
            @Valid @RequestBody CreateBudgetRequest request,
            Authentication auth
    ) {
        String email = (String) auth.getPrincipal();
        BudgetResponse response = createBudgetUseCase.execute(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
