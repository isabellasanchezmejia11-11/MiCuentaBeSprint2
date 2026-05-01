package com.fabricaescuela.micuenta.interfaces.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fabricaescuela.micuenta.application.dto.response.DashboardSummaryResponse;
import com.fabricaescuela.micuenta.application.usecase.GetMonthlyDashboardSummaryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/dashboard")
@Tag(name = "Dashboard", description = "Endpoints para obtener resúmenes y análisis")
@SecurityRequirement(name = "bearerAuth")
public class DashboardController {

    private final GetMonthlyDashboardSummaryUseCase getMonthlyDashboardSummaryUseCase;

    public DashboardController(GetMonthlyDashboardSummaryUseCase getMonthlyDashboardSummaryUseCase) {
        this.getMonthlyDashboardSummaryUseCase = getMonthlyDashboardSummaryUseCase;
    }

    @GetMapping("/monthly-summary")
    @Operation(summary = "Obtener resumen mensual", description = "Retorna un resumen de ingresos, gastos y presupuestos del mes actual")
    @ApiResponse(responseCode = "200", description = "Resumen mensual")
    public ResponseEntity<DashboardSummaryResponse> getMonthlySummary(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        DashboardSummaryResponse response = getMonthlyDashboardSummaryUseCase.execute(email);
        return ResponseEntity.ok(response);
    }
}