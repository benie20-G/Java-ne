package com.spring.best.erp.repositories;

import com.spring.Best.erp.enums.Account;
import com.spring.Best.erp.models.Employment;
import com.spring.Best.erp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Employment entities.
 *
 * @author Best Backend
 * @since 1.0
 */
@Repository
public interface EmploymentRepository extends JpaRepository<Employment, Long> {
    /**
     * Find an employment record by its code.
     *
     * @param code the employment code
     * @return an Optional containing the employment record if found
     */
    Optional<Employment> findByCode(String code);

    /**
     * Find an employment record by employee.
     *
     * @param employee the employee
     * @return an Optional containing the employment record if found
     */
    Optional<Employment> findByEmployee(User employee);

    /**
     * Find all active employment records.
     *
     * @param status the employment status
     * @return a list of active employment records
     */
    List<Employment> findByStatus(Account status);

    /**
     * Check if an employment record exists for the given employee.
     *
     * @param employee the employee
     * @return true if an employment record exists, false otherwise
     */
    boolean existsByEmployee(User employee);
}