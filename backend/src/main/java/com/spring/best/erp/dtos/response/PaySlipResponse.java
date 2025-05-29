package com.spring.Best.erp.dtos.response;

/**
 * DTO for pay slip response.
 *
 * @param id the unique identifier for the pay slip
 * @param employeeEmail the email of the employee
 * @param employeeName the full name of the employee
 * @param housingAmount the housing amount (14% of base salary)
 * @param transportAmount the transport amount (14% of base salary)
 * @param employeeTaxAmount the employee tax amount (30% of base salary)
 * @param pensionAmount the pension amount (6% of base salary)
 * @param medicalInsuranceAmount the medical insurance amount (5% of base salary)
 * @param otherTaxAmount the other tax amount (5% of base salary)
 * @param grossSalary the gross salary (base salary + housing + transport)
 * @param netSalary the net salary (gross salary - all deductions)
 * @param month the month for which the pay slip is generated
 * @param year the year for which the pay slip is generated
 * @param status the status of the pay slip (PENDING or PAID)
 *
 * @author Best Backend
 * @since 1.0
 */
public record PaySlipResponse(
        Long id,
        String employeeEmail,
        String employeeName,
        Double housingAmount,
        Double transportAmount,
        Double employeeTaxAmount,
        Double pensionAmount,
        Double medicalInsuranceAmount,
        Double otherTaxAmount,
        Double grossSalary,
        Double netSalary,
        Integer month,
        Integer year,
        String status
) {
}