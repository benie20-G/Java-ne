package com.spring.Best.erp.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO for deduction creation and update requests.
 *
 * @param name the name of the deduction (e.g., EmployeeTax, Pension, MedicalInsurance)
 * @param percentage the percentage rate of the deduction
 *
 * @author Best Backend
 * @since 1.0
 */
public record DeductionRequest(
        @NotBlank(message = "Deduction name is required")
        String name,

        @NotNull(message = "Percentage is required")
        @Positive(message = "Percentage must be positive")
        Double percentage
) {
}