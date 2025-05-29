package com.spring.Best.erp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class representing a payslip in the ERP system.
 * <p>
 * This class stores information about employee payslips including
 * salary details, deductions, gross salary, net salary, and payment status.
 * </p>
 *
 * @author Best Backend
 * @version 1.0
 * @since 1.0
 */
@Data
@Entity
@Table(name = "pay_slips")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaySlip {
    /**
     * The unique identifier for the payslip.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The employee associated with this payslip.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private User employee;

    /**
     * The housing amount (14% of base salary).
     */
    @Column(name = "housing_amount", nullable = false)
    private Double housingAmount;

    /**
     * The transport amount (14% of base salary).
     */
    @Column(name = "transport_amount", nullable = false)
    private Double transportAmount;

    /**
     * The employee tax amount (30% of base salary).
     */
    @Column(name = "employee_tax_amount", nullable = false)
    private Double employeeTaxAmount;

    /**
     * The pension amount (6% of base salary).
     */
    @Column(name = "pension_amount", nullable = false)
    private Double pensionAmount;

    /**
     * The medical insurance amount (5% of base salary).
     */
    @Column(name = "medical_insurance_amount", nullable = false)
    private Double medicalInsuranceAmount;

    /**
     * The other tax amount (5% of base salary).
     */
    @Column(name = "other_tax_amount", nullable = false)
    private Double otherTaxAmount;

    /**
     * The gross salary (base salary + housing + transport).
     */
    @Column(name = "gross_salary", nullable = false)
    private Double grossSalary;

    /**
     * The net salary (gross salary - all deductions).
     */
    @Column(name = "net_salary", nullable = false)
    private Double netSalary;

    /**
     * The month for which this payslip is generated.
     */
    @Column(name = "month", nullable = false)
    private Integer month;

    /**
     * The year for which this payslip is generated.
     */
    @Column(name = "year", nullable = false)
    private Integer year;

    /**
     * The status of the payslip (pending or paid).
     */
    @Column(name = "status", nullable = false)
    private String status = "PENDING";
}