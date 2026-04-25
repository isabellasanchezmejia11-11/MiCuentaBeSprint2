package com.fabricaescuela.micuenta.interfaces.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fabricaescuela.micuenta.application.dto.response.DashboardSummaryResponse;
import com.fabricaescuela.micuenta.application.usecase.GetMonthlyDashboardSummaryUseCase;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final GetMonthlyDashboardSummaryUseCase getMonthlyDashboardSummaryUseCase;

    public DashboardController(GetMonthlyDashboardSummaryUseCase getMonthlyDashboardSummaryUseCase) {
        this.getMonthlyDashboardSummaryUseCase = getMonthlyDashboardSummaryUseCase;
    }

    @GetMapping("/monthly-summary")
    public ResponseEntity<DashboardSummaryResponse> getMonthlySummary(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        DashboardSummaryResponse response = getMonthlyDashboardSummaryUseCase.execute(email);
        return ResponseEntity.ok(response);
    }
}