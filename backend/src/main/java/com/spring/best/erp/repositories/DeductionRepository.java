package com.spring.best.erp.repositories;

import com.spring.Best.erp.models.Deduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing Deduction entities.
 *
 * @author Best Backend
 * @since 1.0
 */
@Repository
public interface DeductionRepository extends JpaRepository<Deduction, Long> {
    /**
     * Find a deduction by its code.
     *
     * @param code the deduction code
     * @return an Optional containing the deduction if found
     */
    Optional<Deduction> findByCode(String code);

    /**
     * Find a deduction by its name.
     *
     * @param name the deduction name
     * @return an Optional containing the deduction if found
     */
    Optional<Deduction> findByName(String name);

    /**
     * Check if a deduction exists with the given name.
     *
     * @param name the deduction name
     * @return true if a deduction exists with the given name, false otherwise
     */
    boolean existsByName(String name);
}