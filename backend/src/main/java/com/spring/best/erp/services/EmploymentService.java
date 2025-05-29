package com.spring.best.erp.services;

import com.spring.Best.erp.dtos.request.EmploymentRequest;
import com.spring.Best.erp.dtos.response.EmploymentResponse;
import com.spring.Best.erp.enums.Account;
import com.spring.Best.erp.models.Employment;
import com.spring.Best.erp.models.User;
import com.spring.best.erp.repositories.EmploymentRepository;
import com.spring.best.erp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing employment records.
 *
 * @author Best Backend
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmploymentService {

    private final EmploymentRepository employmentRepository;
    private final UserRepository userRepository;

    /**
     * Create a new employment record.
     *
     * @param request the employment request
     * @return the created employment response
     */
    @Transactional
    public EmploymentResponse createEmployment(EmploymentRequest request) {
        log.info("Creating employment record for employee with email: {}", request.employeeEmail());

        User employee = userRepository.findByEmail(request.employeeEmail())
                .orElseThrow(() -> {
                    log.error("Employee not found with email: {}", request.employeeEmail());
                    return new IllegalArgumentException("Employee not found with email: " + request.employeeEmail());
                });

        if (employmentRepository.existsByEmployee(employee)) {
            log.error("Employment record already exists for employee with email: {}", request.employeeEmail());
            throw new IllegalStateException("Employment record already exists for employee with email: " + request.employeeEmail());
        }

        Employment employment = Employment.builder()
                .code(generateEmploymentCode())
                .employee(employee)
                .department(request.department())
                .position(request.position())
                .baseSalary(request.baseSalary())
                .joiningDate(request.joiningDate())
                .status(Account.ACTIVE)
                .build();

        Employment savedEmployment = employmentRepository.save(employment);
        log.info("Employment record created successfully for employee with email: {}", request.employeeEmail());

        return mapToEmploymentResponse(savedEmployment);
    }

    /**
     * Get all employment records.
     *
     * @return a list of employment responses
     */
    public List<EmploymentResponse> getAllEmployments() {
        log.info("Fetching all employment records");
        return employmentRepository.findAll().stream()
                .map(this::mapToEmploymentResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all active employment records.
     *
     * @return a list of active employment responses
     */
    public List<EmploymentResponse> getAllActiveEmployments() {
        log.info("Fetching all active employment records");
        return employmentRepository.findByStatus(Account.ACTIVE).stream()
                .map(this::mapToEmploymentResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get an employment record by code.
     *
     * @param code the employment code
     * @return the employment response
     */
    public EmploymentResponse getEmploymentByCode(String code) {
        log.info("Fetching employment record with code: {}", code);
        Employment employment = employmentRepository.findByCode(code)
                .orElseThrow(() -> {
                    log.error("Employment record not found with code: {}", code);
                    return new IllegalArgumentException("Employment record not found with code: " + code);
                });

        return mapToEmploymentResponse(employment);
    }

    /**
     * Get an employment record by employee email.
     *
     * @param email the employee email
     * @return the employment response
     */
    public EmploymentResponse getEmploymentByEmployeeEmail(String email) {
        log.info("Fetching employment record for employee with email: {}", email);
        User employee = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Employee not found with email: {}", email);
                    return new IllegalArgumentException("Employee not found with email: " + email);
                });

        Employment employment = employmentRepository.findByEmployee(employee)
                .orElseThrow(() -> {
                    log.error("Employment record not found for employee with email: {}", email);
                    return new IllegalArgumentException("Employment record not found for employee with email: " + email);
                });

        return mapToEmploymentResponse(employment);
    }

    /**
     * Update an employment record.
     *
     * @param code the employment code
     * @param request the employment request
     * @return the updated employment response
     */
    @Transactional
    public EmploymentResponse updateEmployment(String code, EmploymentRequest request) {
        log.info("Updating employment record with code: {}", code);
        Employment employment = employmentRepository.findByCode(code)
                .orElseThrow(() -> {
                    log.error("Employment record not found with code: {}", code);
                    return new IllegalArgumentException("Employment record not found with code: " + code);
                });

        employment.setDepartment(request.department());
        employment.setPosition(request.position());
        employment.setBaseSalary(request.baseSalary());
        employment.setJoiningDate(request.joiningDate());

        Employment updatedEmployment = employmentRepository.save(employment);
        log.info("Employment record updated successfully with code: {}", code);

        return mapToEmploymentResponse(updatedEmployment);
    }

    /**
     * Activate or deactivate an employment record.
     *
     * @param code the employment code
     * @param active true to activate, false to deactivate
     * @return the updated employment response
     */
    @Transactional
    public EmploymentResponse setEmploymentStatus(String code, boolean active) {
        log.info("Setting employment record status with code: {} to {}", code, active ? "ACTIVE" : "INACTIVE");
        Employment employment = employmentRepository.findByCode(code)
                .orElseThrow(() -> {
                    log.error("Employment record not found with code: {}", code);
                    return new IllegalArgumentException("Employment record not found with code: " + code);
                });

        employment.setStatus(active ? Account.ACTIVE : Account.DISABLED);
        Employment updatedEmployment = employmentRepository.save(employment);
        log.info("Employment record status updated successfully with code: {}", code);

        return mapToEmploymentResponse(updatedEmployment);
    }

    /**
     * Generate a unique employment code.
     *
     * @return a unique employment code
     */
    private String generateEmploymentCode() {
        return "EMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Map an employment entity to an employment response.
     *
     * @param employment the employment entity
     * @return the employment response
     */
    private EmploymentResponse mapToEmploymentResponse(Employment employment) {
        return new EmploymentResponse(
                employment.getId(),
                employment.getCode(),
                employment.getEmployee().getEmail(),
                employment.getEmployee().getFullName(),
                employment.getDepartment(),
                employment.getPosition(),
                employment.getBaseSalary(),
                employment.getJoiningDate(),
                employment.getStatus().toString()
        );
    }
}