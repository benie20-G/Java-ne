package com.spring.Best.erp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class representing deductions in the ERP system.
 * <p>
 * This class stores information about different types of deductions
 * that are applied to employee salaries, including the deduction name
 * and percentage rate.
 * </p>
 *
 * @author Best Backend
 * @version 1.0
 * @since 1.0
 */
@Data
@Entity
@Table(name = "deductions")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Deduction {
    /**
     * The unique identifier for the deduction.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The unique code for the deduction.
     */
    @Column(name = "code", unique = true, nullable = false)
    private String code;

    /**
     * The name of the deduction (e.g., EmployeeTax, Pension, MedicalInsurance).
     */
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    /**
     * The percentage rate of the deduction.
     */
    @Column(name = "percentage", nullable = false)
    private Double percentage;
}