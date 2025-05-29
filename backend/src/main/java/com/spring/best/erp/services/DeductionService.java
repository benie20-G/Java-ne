package com.spring.best.erp.services;

import com.spring.Best.erp.dtos.request.DeductionRequest;
import com.spring.Best.erp.dtos.response.DeductionResponse;
import com.spring.Best.erp.models.Deduction;
import com.spring.best.erp.repositories.DeductionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing deductions.
 *
 * @author Best Backend
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DeductionService {

    private final DeductionRepository deductionRepository;

    /**
     * Create a new deduction.
     *
     * @param request the deduction request
     * @return the created deduction response
     */
    @Transactional
    public DeductionResponse createDeduction(DeductionRequest request) {
        log.info("Creating deduction with name: {}", request.name());

        if (deductionRepository.existsByName(request.name())) {
            log.error("Deduction already exists with name: {}", request.name());
            throw new IllegalStateException("Deduction already exists with name: " + request.name());
        }

        Deduction deduction = Deduction.builder()
                .code(generateDeductionCode())
                .name(request.name())
                .percentage(request.percentage())
                .build();

        Deduction savedDeduction = deductionRepository.save(deduction);
        log.info("Deduction created successfully with name: {}", request.name());

        return mapToDeductionResponse(savedDeduction);
    }

    /**
     * Get all deductions.
     *
     * @return a list of deduction responses
     */
    public List<DeductionResponse> getAllDeductions() {
        log.info("Fetching all deductions");
        return deductionRepository.findAll().stream()
                .map(this::mapToDeductionResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get a deduction by code.
     *
     * @param code the deduction code
     * @return the deduction response
     */
    public DeductionResponse getDeductionByCode(String code) {
        log.info("Fetching deduction with code: {}", code);
        Deduction deduction = deductionRepository.findByCode(code)
                .orElseThrow(() -> {
                    log.error("Deduction not found with code: {}", code);
                    return new IllegalArgumentException("Deduction not found with code: " + code);
                });

        return mapToDeductionResponse(deduction);
    }

    /**
     * Get a deduction by name.
     *
     * @param name the deduction name
     * @return the deduction response
     */
    public DeductionResponse getDeductionByName(String name) {
        log.info("Fetching deduction with name: {}", name);
        Deduction deduction = deductionRepository.findByName(name)
                .orElseThrow(() -> {
                    log.error("Deduction not found with name: {}", name);
                    return new IllegalArgumentException("Deduction not found with name: " + name);
                });

        return mapToDeductionResponse(deduction);
    }

    /**
     * Update a deduction.
     *
     * @param code the deduction code
     * @param request the deduction request
     * @return the updated deduction response
     */
    @Transactional
    public DeductionResponse updateDeduction(String code, DeductionRequest request) {
        log.info("Updating deduction with code: {}", code);
        Deduction deduction = deductionRepository.findByCode(code)
                .orElseThrow(() -> {
                    log.error("Deduction not found with code: {}", code);
                    return new IllegalArgumentException("Deduction not found with code: " + code);
                });

        // Check if the new name already exists for a different deduction
        if (!deduction.getName().equals(request.name()) && deductionRepository.existsByName(request.name())) {
            log.error("Deduction already exists with name: {}", request.name());
            throw new IllegalStateException("Deduction already exists with name: " + request.name());
        }

        deduction.setName(request.name());
        deduction.setPercentage(request.percentage());

        Deduction updatedDeduction = deductionRepository.save(deduction);
        log.info("Deduction updated successfully with code: {}", code);

        return mapToDeductionResponse(updatedDeduction);
    }

    /**
     * Delete a deduction.
     *
     * @param code the deduction code
     */
    @Transactional
    public void deleteDeduction(String code) {
        log.info("Deleting deduction with code: {}", code);
        Deduction deduction = deductionRepository.findByCode(code)
                .orElseThrow(() -> {
                    log.error("Deduction not found with code: {}", code);
                    return new IllegalArgumentException("Deduction not found with code: " + code);
                });

        deductionRepository.delete(deduction);
        log.info("Deduction deleted successfully with code: {}", code);
    }

    /**
     * Initialize default deductions if they don't exist.
     */
    @Transactional
    public void initializeDefaultDeductions() {
        log.info("Initializing default deductions");

        // Employee Tax (30%)
        createDeductionIfNotExists("EmployeeTax", 30.0);

        // Pension (6%)
        createDeductionIfNotExists("Pension", 6.0);

        // Medical Insurance (5%)
        createDeductionIfNotExists("MedicalInsurance", 5.0);

        // Others (5%)
        createDeductionIfNotExists("Others", 5.0);

        // Housing (14%)
        createDeductionIfNotExists("Housing", 14.0);

        // Transport (14%)
        createDeductionIfNotExists("Transport", 14.0);

        log.info("Default deductions initialized successfully");
    }

    /**
     * Create a deduction if it doesn't exist.
     *
     * @param name the deduction name
     * @param percentage the deduction percentage
     */
    private void createDeductionIfNotExists(String name, Double percentage) {
        if (!deductionRepository.existsByName(name)) {
            Deduction deduction = Deduction.builder()
                    .code(generateDeductionCode())
                    .name(name)
                    .percentage(percentage)
                    .build();

            deductionRepository.save(deduction);
            log.info("Created default deduction: {} with percentage: {}", name, percentage);
        }
    }

    /**
     * Generate a unique deduction code.
     *
     * @return a unique deduction code
     */
    private String generateDeductionCode() {
        return "DED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Map a deduction entity to a deduction response.
     *
     * @param deduction the deduction entity
     * @return the deduction response
     */
    private DeductionResponse mapToDeductionResponse(Deduction deduction) {
        return new DeductionResponse(
                deduction.getId(),
                deduction.getCode(),
                deduction.getName(),
                deduction.getPercentage()
        );
    }
}