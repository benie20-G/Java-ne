package com.spring.Best.erp.models;

import com.spring.Best.erp.enums.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entity class representing employment details in the ERP system.
 * <p>
 * This class stores employment information for employees including department,
 * position, base salary, and employment status.
 * </p>
 *
 * @author Best Backend
 * @version 1.0
 * @since 1.0
 */
@Data
@Entity
@Table(name = "employments")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employment {
    /**
     * The unique identifier for the employment record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The unique code for the employment record.
     */
    @Column(name = "code", unique = true, nullable = false)
    private String code;

    /**
     * The employee associated with this employment record.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private User employee;

    /**
     * The department where the employee works.
     */
    @Column(name = "department", nullable = false)
    private String department;

    /**
     * The position held by the employee.
     */
    @Column(name = "position", nullable = false)
    private String position;

    /**
     * The base salary of the employee.
     */
    @Column(name = "base_salary", nullable = false)
    private Double baseSalary;

    /**
     * The date when the employee joined the organization.
     */
    @Column(name = "joining_date", nullable = false)
    private LocalDate joiningDate;

    /**
     * The current status of the employment (active or inactive).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Account status = Account.ACTIVE;
}