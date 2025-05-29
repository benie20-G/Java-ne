package com.spring.Best.erp.dtos.response;

/**
 * DTO for deduction response.
 *
 * @param id the unique identifier for the deduction
 * @param code the unique code for the deduction
 * @param name the name of the deduction (e.g., EmployeeTax, Pension, MedicalInsurance)
 * @param percentage the percentage rate of the deduction
 *
 * @author Best Backend
 * @since 1.0
 */
public record DeductionResponse(
        Long id,
        String code,
        String name,
        Double percentage
) {
}