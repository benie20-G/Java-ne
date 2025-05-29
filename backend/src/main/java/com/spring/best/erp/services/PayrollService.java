package com.spring.best.erp.services;

import com.spring.Best.erp.dtos.request.PaySlipRequest;
import com.spring.Best.erp.dtos.response.PaySlipResponse;
import com.spring.Best.erp.models.Deduction;
import com.spring.Best.erp.models.Employment;
import com.spring.Best.erp.models.PaySlip;
import com.spring.Best.erp.models.User;
import com.spring.best.erp.repositories.DeductionRepository;
import com.spring.best.erp.repositories.EmploymentRepository;
import com.spring.best.erp.repositories.PaySlipRepository;
import com.spring.best.erp.repositories.UserRepository;
import com.spring.Best.erp.enums.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service for managing payroll calculations and payslip generation.
 *
 * @author Best Backend
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PayrollService {

    private final PaySlipRepository paySlipRepository;
    private final EmploymentRepository employmentRepository;
    private final UserRepository userRepository;
    private final DeductionRepository deductionRepository;
    private final DeductionService deductionService;

    /**
     * Generate payslips for all active employees for a specific month and year.
     *
     * @param request the payslip request containing month and year
     * @return a list of generated payslip responses
     */
    @Transactional
    public List<PaySlipResponse> generatePaySlips(PaySlipRequest request) {
        log.info("Generating pay slips for month: {} and year: {}", request.month(), request.year());

        // Ensure default deductions exist
        deductionService.initializeDefaultDeductions();

        // Get all active employments
        List<Employment> activeEmployments = employmentRepository.findByStatus(Account.ACTIVE);
        if (activeEmployments.isEmpty()) {
            log.warn("No active employments found");
            return new ArrayList<>();
        }

        // Get all deductions
        Map<String, Deduction> deductionMap = deductionRepository.findAll().stream()
                .collect(Collectors.toMap(Deduction::getName, Function.identity()));

        // Check if required deductions exist
        checkRequiredDeductions(deductionMap);

        List<PaySlipResponse> responses = new ArrayList<>();

        for (Employment employment : activeEmployments) {
            User employee = employment.getEmployee();

            // Check if payslip already exists for this employee, month, and year
            if (paySlipRepository.existsByEmployeeAndMonthAndYear(employee, request.month(), request.year())) {
                log.warn("Pay slip already exists for employee: {}, month: {}, year: {}",
                        employee.getEmail(), request.month(), request.year());
                continue;
            }

            // Calculate pay slip amounts
            double baseSalary = employment.getBaseSalary();
            double housingAmount = calculatePercentage(baseSalary, deductionMap.get("Housing").getPercentage());
            double transportAmount = calculatePercentage(baseSalary, deductionMap.get("Transport").getPercentage());
            double grossSalary = baseSalary + housingAmount + transportAmount;

            double employeeTaxAmount = calculatePercentage(baseSalary, deductionMap.get("EmployeeTax").getPercentage());
            double pensionAmount = calculatePercentage(baseSalary, deductionMap.get("Pension").getPercentage());
            double medicalInsuranceAmount = calculatePercentage(baseSalary, deductionMap.get("MedicalInsurance").getPercentage());
            double otherTaxAmount = calculatePercentage(baseSalary, deductionMap.get("Others").getPercentage());

            double totalDeductions = employeeTaxAmount + pensionAmount + medicalInsuranceAmount + otherTaxAmount;
            double netSalary = grossSalary - totalDeductions;

            // Create and save payslip
            PaySlip paySlip = PaySlip.builder()
                    .employee(employee)
                    .housingAmount(housingAmount)
                    .transportAmount(transportAmount)
                    .employeeTaxAmount(employeeTaxAmount)
                    .pensionAmount(pensionAmount)
                    .medicalInsuranceAmount(medicalInsuranceAmount)
                    .otherTaxAmount(otherTaxAmount)
                    .grossSalary(grossSalary)
                    .netSalary(netSalary)
                    .month(request.month())
                    .year(request.year())
                    .status("PENDING")
                    .build();

            PaySlip savedPaySlip = paySlipRepository.save(paySlip);
            responses.add(mapToPaySlipResponse(savedPaySlip));
            log.info("Generated pay slip for employee: {}, month: {}, year: {}",
                    employee.getEmail(), request.month(), request.year());
        }

        return responses;
    }

    /**
     * Get all payslips for a specific month and year.
     *
     * @param month the month
     * @param year the year
     * @return a list of payslip responses
     */
    public List<PaySlipResponse> getPaySlipsByMonthAndYear(Integer month, Integer year) {
        log.info("Fetching pay slips for month: {} and year: {}", month, year);
        return paySlipRepository.findByMonthAndYear(month, year).stream()
                .map(this::mapToPaySlipResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get a payslip for a specific employee, month, and year.
     *
     * @param email the employee email
     * @param month the month
     * @param year the year
     * @return the payslip response
     */
    public PaySlipResponse getPaySlipByEmployeeAndMonthAndYear(String email, Integer month, Integer year) {
        log.info("Fetching pay slip for employee: {}, month: {}, year: {}", email, month, year);
        User employee = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Employee not found with email: {}", email);
                    return new IllegalArgumentException("Employee not found with email: " + email);
                });

        PaySlip paySlip = paySlipRepository.findByEmployeeAndMonthAndYear(employee, month, year)
                .orElseThrow(() -> {
                    log.error("Pay slip not found for employee: {}, month: {}, year: {}", email, month, year);
                    return new IllegalArgumentException("Pay slip not found for employee: " + email +
                            ", month: " + month + ", year: " + year);
                });

        return mapToPaySlipResponse(paySlip);
    }

    /**
     * Get all payslips for a specific employee.
     *
     * @param email the employee email
     * @return a list of payslip responses
     */
    public List<PaySlipResponse> getPaySlipsByEmployee(String email) {
        log.info("Fetching pay slips for employee: {}", email);
        User employee = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Employee not found with email: {}", email);
                    return new IllegalArgumentException("Employee not found with email: " + email);
                });

        return paySlipRepository.findByEmployee(employee).stream()
                .map(this::mapToPaySlipResponse)
                .collect(Collectors.toList());
    }

    /**
     * Approve a payslip.
     *
     * @param id the payslip id
     * @return the updated payslip response
     */
    @Transactional
    public PaySlipResponse approvePaySlip(Long id) {
        log.info("Approving pay slip with id: {}", id);
        PaySlip paySlip = paySlipRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Pay slip not found with id: {}", id);
                    return new IllegalArgumentException("Pay slip not found with id: " + id);
                });

        paySlip.setStatus("PAID");
        PaySlip updatedPaySlip = paySlipRepository.save(paySlip);
        log.info("Pay slip approved successfully with id: {}", id);

        return mapToPaySlipResponse(updatedPaySlip);
    }

    /**
     * Approve all payslips for a specific month and year.
     *
     * @param month the month
     * @param year the year
     * @return a list of updated payslip responses
     */
    @Transactional
    public List<PaySlipResponse> approveAllPaySlips(Integer month, Integer year) {
        log.info("Approving all pay slips for month: {} and year: {}", month, year);
        List<PaySlip> paySlips = paySlipRepository.findByMonthAndYear(month, year);
        if (paySlips.isEmpty()) {
            log.warn("No pay slips found for month: {} and year: {}", month, year);
            return new ArrayList<>();
        }

        List<PaySlipResponse> responses = new ArrayList<>();
        for (PaySlip paySlip : paySlips) {
            paySlip.setStatus("PAID");
            PaySlip updatedPaySlip = paySlipRepository.save(paySlip);
            responses.add(mapToPaySlipResponse(updatedPaySlip));
        }

        log.info("Approved {} pay slips for month: {} and year: {}", paySlips.size(), month, year);
        return responses;
    }

    /**
     * Calculate a percentage of a value.
     *
     * @param value the value
     * @param percentage the percentage
     * @return the calculated amount
     */
    private double calculatePercentage(double value, double percentage) {
        return (value * percentage) / 100.0;
    }

    /**
     * Check if required deductions exist.
     *
     * @param deductionMap the map of deductions
     * @throws IllegalStateException if a required deduction is missing
     */
    private void checkRequiredDeductions(Map<String, Deduction> deductionMap) {
        List<String> requiredDeductions = List.of(
                "EmployeeTax", "Pension", "MedicalInsurance", "Others", "Housing", "Transport"
        );

        for (String deductionName : requiredDeductions) {
            if (!deductionMap.containsKey(deductionName)) {
                log.error("Required deduction not found: {}", deductionName);
                throw new IllegalStateException("Required deduction not found: " + deductionName);
            }
        }
    }

    /**
     * Map a payslip entity to a payslip response.
     *
     * @param paySlip the payslip entity
     * @return the payslip response
     */
    private PaySlipResponse mapToPaySlipResponse(PaySlip paySlip) {
        return new PaySlipResponse(
                paySlip.getId(),
                paySlip.getEmployee().getEmail(),
                paySlip.getEmployee().getFullName(),
                paySlip.getHousingAmount(),
                paySlip.getTransportAmount(),
                paySlip.getEmployeeTaxAmount(),
                paySlip.getPensionAmount(),
                paySlip.getMedicalInsuranceAmount(),
                paySlip.getOtherTaxAmount(),
                paySlip.getGrossSalary(),
                paySlip.getNetSalary(),
                paySlip.getMonth(),
                paySlip.getYear(),
                paySlip.getStatus()
        );
    }
}