package com.spring.Best.erp.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

/**
 * DTO for employment creation and update requests.
 *
 * @param employeeEmail the email of the employee
 * @param department the department where the employee works
 * @param position the position held by the employee
 * @param baseSalary the base salary of the employee
 * @param joiningDate the date when the employee joined the organization
 *
 * @author Best Backend
 * @since 1.0
 */
public record EmploymentRequest(
        @NotBlank(message = "Employee email is required")
        String employeeEmail,

        @NotBlank(message = "Department is required")
        String department,

        @NotBlank(message = "Position is required")
        String position,

        @NotNull(message = "Base salary is required")
        @Positive(message = "Base salary must be positive")
        Double baseSalary,

        @NotNull(message = "Joining date is required")
        LocalDate joiningDate
) {
}