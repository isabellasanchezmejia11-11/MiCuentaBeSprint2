package com.fabricaescuela.micuenta.interfaces.rest;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fabricaescuela.micuenta.application.dto.request.CreateMovementRequest;
import com.fabricaescuela.micuenta.application.dto.request.UpdateMovementRequest;
import com.fabricaescuela.micuenta.application.dto.response.MovementResponse;
import com.fabricaescuela.micuenta.application.dto.response.MovementsListResponse;
import com.fabricaescuela.micuenta.application.usecase.DeleteMovementUseCase;
import com.fabricaescuela.micuenta.application.usecase.ListExpensesUseCase;
import com.fabricaescuela.micuenta.application.usecase.ListIncomesUseCase;
import com.fabricaescuela.micuenta.application.usecase.ListMovementsUseCase;
import com.fabricaescuela.micuenta.application.usecase.RegisterExpenseUseCase;
import com.fabricaescuela.micuenta.application.usecase.RegisterIncomeUseCase;
import com.fabricaescuela.micuenta.application.usecase.UpdateMovementUseCase;
import com.fabricaescuela.micuenta.domain.model.MovementType;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/movements")
public class MovementController {

    private final RegisterIncomeUseCase registerIncomeUseCase;
    private final RegisterExpenseUseCase registerExpenseUseCase;
    private final UpdateMovementUseCase updateMovementUseCase;
    private final DeleteMovementUseCase deleteMovementUseCase;
    private final ListIncomesUseCase listIncomesUseCase;
    private final ListExpensesUseCase listExpensesUseCase;
    private final ListMovementsUseCase listMovementsUseCase;

    public MovementController(
            RegisterIncomeUseCase registerIncomeUseCase,
            RegisterExpenseUseCase registerExpenseUseCase,
            UpdateMovementUseCase updateMovementUseCase,
            DeleteMovementUseCase deleteMovementUseCase,
            ListIncomesUseCase listIncomesUseCase,
            ListExpensesUseCase listExpensesUseCase,
            ListMovementsUseCase listMovementsUseCase
    ) {
        this.registerIncomeUseCase = registerIncomeUseCase;
        this.registerExpenseUseCase = registerExpenseUseCase;
        this.updateMovementUseCase = updateMovementUseCase;
        this.deleteMovementUseCase = deleteMovementUseCase;
        this.listIncomesUseCase = listIncomesUseCase;
        this.listExpensesUseCase = listExpensesUseCase;
        this.listMovementsUseCase = listMovementsUseCase;
    }

    @PostMapping("/incomes")
    public ResponseEntity<MovementResponse> registerIncome(
            @Valid @RequestBody CreateMovementRequest request,
            Authentication authentication
    ) {
        String email = (String) authentication.getPrincipal();
        MovementResponse response = registerIncomeUseCase.execute(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/expenses")
    public ResponseEntity<MovementResponse> registerExpense(
            @Valid @RequestBody CreateMovementRequest request,
            Authentication authentication
    ) {
        String email = (String) authentication.getPrincipal();
        MovementResponse response = registerExpenseUseCase.execute(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/incomes")
    public ResponseEntity<List<MovementResponse>> listIncomes(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        return ResponseEntity.ok(listIncomesUseCase.execute(email));
    }

    @GetMapping("/expenses")
    public ResponseEntity<List<MovementResponse>> listExpenses(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        return ResponseEntity.ok(listExpensesUseCase.execute(email));
    }

    @GetMapping
    public ResponseEntity<MovementsListResponse> listMovements(
            @RequestParam(required = false) MovementType type,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Authentication authentication
    ) {
        String email = (String) authentication.getPrincipal();
        List<MovementResponse> movements = listMovementsUseCase.execute(email, type, categoryId, startDate, endDate);

        if (movements.isEmpty()) {
            return ResponseEntity.ok(MovementsListResponse.noResults());
        }

        return ResponseEntity.ok(MovementsListResponse.withData(movements));
    }

    @PutMapping("/{movementId}")
    public ResponseEntity<MovementResponse> updateMovement(
            @PathVariable Long movementId,
            @Valid @RequestBody UpdateMovementRequest request,
            Authentication authentication
    ) {
        String email = (String) authentication.getPrincipal();
        MovementResponse response = updateMovementUseCase.execute(movementId, email, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{movementId}")
    public ResponseEntity<Void> deleteMovement(
            @PathVariable Long movementId,
            Authentication authentication
    ) {
        String email = (String) authentication.getPrincipal();
        deleteMovementUseCase.execute(movementId, email);
        return ResponseEntity.noContent().build();
    }
}