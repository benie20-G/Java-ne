package com.spring.Best.erp.dtos.response;

import java.time.LocalDate;

/**
 * DTO for employment response.
 *
 * @param id the unique identifier for the employment record
 * @param code the unique code for the employment record
 * @param employeeEmail the email of the employee
 * @param employeeName the full name of the employee
 * @param department the department where the employee works
 * @param position the position held by the employee
 * @param baseSalary the base salary of the employee
 * @param joiningDate the date when the employee joined the organization
 * @param status the current status of the employment (ACTIVE or INACTIVE)
 *
 * @author Best Backend
 * @since 1.0
 */
public record EmploymentResponse(
        Long id,
        String code,
        String employeeEmail,
        String employeeName,
        String department,
        String position,
        Double baseSalary,
        LocalDate joiningDate,
        String status
) {
}