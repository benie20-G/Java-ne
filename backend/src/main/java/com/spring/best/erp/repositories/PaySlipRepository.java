package com.spring.best.erp.repositories;

import com.spring.Best.erp.models.PaySlip;
import com.spring.Best.erp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing PaySlip entities.
 *
 * @author Best Backend
 * @since 1.0
 */
@Repository
public interface PaySlipRepository extends JpaRepository<PaySlip, Long> {
    /**
     * Find all payslips for a specific employee.
     *
     * @param employee the employee
     * @return a list of payslips for the employee
     */
    List<PaySlip> findByEmployee(User employee);

    /**
     * Find all payslips for a specific month and year.
     *
     * @param month the month
     * @param year the year
     * @return a list of payslips for the month and year
     */
    List<PaySlip> findByMonthAndYear(Integer month, Integer year);

    /**
     * Find all payslips for a specific employee, month, and year.
     *
     * @param employee the employee
     * @param month the month
     * @param year the year
     * @return a list of payslips for the employee, month, and year
     */
    Optional<PaySlip> findByEmployeeAndMonthAndYear(User employee, Integer month, Integer year);

    /**
     * Find all payslips with a specific status.
     *
     * @param status the status
     * @return a list of payslips with the status
     */
    List<PaySlip> findByStatus(String status);

    /**
     * Find all payslips for a specific employee with a specific status.
     *
     * @param employee the employee
     * @param status the status
     * @return a list of payslips for the employee with the status
     */
    List<PaySlip> findByEmployeeAndStatus(User employee, String status);

    /**
     * Find all payslips for a specific month, year, and status.
     *
     * @param month the month
     * @param year the year
     * @param status the status
     * @return a list of payslips for the month, year, and status
     */
    List<PaySlip> findByMonthAndYearAndStatus(Integer month, Integer year, String status);

    /**
     * Check if a payslip exists for a specific employee, month, and year.
     *
     * @param employee the employee
     * @param month the month
     * @param year the year
     * @return true if a payslip exists, false otherwise
     */
    boolean existsByEmployeeAndMonthAndYear(User employee, Integer month, Integer year);
}
