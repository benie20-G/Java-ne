package com.spring.best.erp.controllers;

import com.spring.Best.erp.dtos.request.PaySlipRequest;
import com.spring.Best.erp.dtos.response.PaySlipResponse;
import com.spring.best.erp.services.MessageService;
import com.spring.best.erp.services.PayrollService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing payroll processing.
 *
 * @author Best Backend
 * @since 1.0
 */
@Tag(name = "Payroll", description = "Endpoints for managing payroll processing")
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/payroll")
public class PayrollController {

    private final PayrollService payrollService;
    private final MessageService messageService;

    /**
     * Generate payslips for all active employees for a specific month and year.
     *
     * @param request the payslip request
     * @return a response with the generated payslips
     */
    @Operation(summary = "Generate pay slips", description = "Generates pay slips for all active employees for a specific month and year")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pay slips generated successfully",
                    content = @Content(schema = @Schema(implementation = PaySlipResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping("/generate")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<List<PaySlipResponse>> generatePaySlips(@Valid @RequestBody PaySlipRequest request) {
        log.info("Generating pay slips for month: {} and year: {}", request.month(), request.year());
        List<PaySlipResponse> responses = payrollService.generatePaySlips(request);
        return ResponseEntity.ok(responses);
    }

    /**
     * Get all payslips for a specific month and year.
     *
     * @param month the month
     * @param year the year
     * @return a response with the payslips
     */
    @Operation(summary = "Get pay slips by month and year", description = "Retrieves all pay slips for a specific month and year")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pay slips retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PaySlipResponse.class)))
    })
    @GetMapping("/{month}/{year}")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<List<PaySlipResponse>> getPaySlipsByMonthAndYear(
            @PathVariable Integer month, @PathVariable Integer year) {
        log.info("Fetching pay slips for month: {} and year: {}", month, year);
        List<PaySlipResponse> responses = payrollService.getPaySlipsByMonthAndYear(month, year);
        return ResponseEntity.ok(responses);
    }

    /**
     * Get a payslip for a specific employee, month, and year.
     *
     * @param email the employee email
     * @param month the month
     * @param year the year
     * @return a response with the payslip
     */
    @Operation(summary = "Get a pay slip by employee, month, and year", description = "Retrieves a pay slip for a specific employee, month, and year")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pay slip retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PaySlipResponse.class))),
            @ApiResponse(responseCode = "404", description = "Pay slip not found")
    })
    @GetMapping("/employee/{email}/{month}/{year}")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN') or (hasRole('ROLE_EMPLOYEE') and #email == authentication.name)")
    public ResponseEntity<PaySlipResponse> getPaySlipByEmployeeAndMonthAndYear(
            @PathVariable String email, @PathVariable Integer month, @PathVariable Integer year) {
        log.info("Fetching pay slip for employee: {}, month: {}, year: {}", email, month, year);
        PaySlipResponse response = payrollService.getPaySlipByEmployeeAndMonthAndYear(email, month, year);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all payslips for a specific employee.
     *
     * @param email the employee email
     * @return a response with the payslips
     */
    @Operation(summary = "Get pay slips by employee", description = "Retrieves all pay slips for a specific employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pay slips retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PaySlipResponse.class))),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/employee/{email}")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN') or (hasRole('ROLE_EMPLOYEE') and #email == authentication.name)")
    public ResponseEntity<List<PaySlipResponse>> getPaySlipsByEmployee(@PathVariable String email) {
        log.info("Fetching pay slips for employee: {}", email);
        List<PaySlipResponse> responses = payrollService.getPaySlipsByEmployee(email);
        return ResponseEntity.ok(responses);
    }

    /**
     * Approve a payslip.
     *
     * @param id the payslip id
     * @return a response with the updated payslip
     */
    @Operation(summary = "Approve a pay slip", description = "Approves a pay slip by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pay slip approved successfully",
                    content = @Content(schema = @Schema(implementation = PaySlipResponse.class))),
            @ApiResponse(responseCode = "404", description = "Pay slip not found")
    })
    @PatchMapping("/approve/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PaySlipResponse> approvePaySlip(@PathVariable Long id) {
        log.info("Approving pay slip with id: {}", id);
        PaySlipResponse response = payrollService.approvePaySlip(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Approve all payslips for a specific month and year.
     *
     * @param month the month
     * @param year the year
     * @return a response with the updated payslips
     */
    @Operation(summary = "Approve all pay slips", description = "Approves all pay slips for a specific month and year")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pay slips approved successfully",
                    content = @Content(schema = @Schema(implementation = PaySlipResponse.class)))
    })
    @PatchMapping("/approve/{month}/{year}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<PaySlipResponse>> approveAllPaySlips(
            @PathVariable Integer month, @PathVariable Integer year) {
        log.info("Approving all pay slips for month: {} and year: {}", month, year);
        List<PaySlipResponse> responses = payrollService.approveAllPaySlips(month, year);
        
        // Generate and send notification messages for approved payslips
        messageService.generateMessagesForApprovedPaySlips(month, year);
        messageService.sendPendingMessages();
        
        return ResponseEntity.ok(responses);
    }
}