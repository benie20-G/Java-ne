package com.spring.best.erp.controllers;

import com.spring.Best.erp.dtos.request.EmploymentRequest;
import com.spring.Best.erp.dtos.response.EmploymentResponse;
import com.spring.best.erp.services.EmploymentService;
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
 * Controller for managing employment records.
 *
 * @author Best Backend
 * @since 1.0
 */
@Tag(name = "Employment", description = "Endpoints for managing employment records")
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/employments")
public class EmploymentController {

    private final EmploymentService employmentService;

    /**
     * Create a new employment record.
     *
     * @param request the employment request
     * @return a response with the created employment record
     */
    @Operation(summary = "Create a new employment record", description = "Creates a new employment record for an employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employment record created successfully",
                    content = @Content(schema = @Schema(implementation = EmploymentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<EmploymentResponse> createEmployment(@Valid @RequestBody EmploymentRequest request) {
        log.info("Creating employment record for employee with email: {}", request.employeeEmail());
        EmploymentResponse response = employmentService.createEmployment(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all employment records.
     *
     * @return a response with all employment records
     */
    @Operation(summary = "Get all employment records", description = "Retrieves all employment records")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employment records retrieved successfully",
                    content = @Content(schema = @Schema(implementation = EmploymentResponse.class)))
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<List<EmploymentResponse>> getAllEmployments() {
        log.info("Fetching all employment records");
        List<EmploymentResponse> responses = employmentService.getAllEmployments();
        return ResponseEntity.ok(responses);
    }

    /**
     * Get all active employment records.
     *
     * @return a response with all active employment records
     */
    @Operation(summary = "Get all active employment records", description = "Retrieves all active employment records")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active employment records retrieved successfully",
                    content = @Content(schema = @Schema(implementation = EmploymentResponse.class)))
    })
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<List<EmploymentResponse>> getAllActiveEmployments() {
        log.info("Fetching all active employment records");
        List<EmploymentResponse> responses = employmentService.getAllActiveEmployments();
        return ResponseEntity.ok(responses);
    }

    /**
     * Get an employment record by code.
     *
     * @param code the employment code
     * @return a response with the employment record
     */
    @Operation(summary = "Get an employment record by code", description = "Retrieves an employment record by its code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employment record retrieved successfully",
                    content = @Content(schema = @Schema(implementation = EmploymentResponse.class))),
            @ApiResponse(responseCode = "404", description = "Employment record not found")
    })
    @GetMapping("/{code}")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<EmploymentResponse> getEmploymentByCode(@PathVariable String code) {
        log.info("Fetching employment record with code: {}", code);
        EmploymentResponse response = employmentService.getEmploymentByCode(code);
        return ResponseEntity.ok(response);
    }

    /**
     * Get an employment record by employee email.
     *
     * @param email the employee email
     * @return a response with the employment record
     */
    @Operation(summary = "Get an employment record by employee email", description = "Retrieves an employment record by employee email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employment record retrieved successfully",
                    content = @Content(schema = @Schema(implementation = EmploymentResponse.class))),
            @ApiResponse(responseCode = "404", description = "Employment record not found")
    })
    @GetMapping("/employee/{email}")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN') or (hasRole('ROLE_EMPLOYEE') and #email == authentication.name)")
    public ResponseEntity<EmploymentResponse> getEmploymentByEmployeeEmail(@PathVariable String email) {
        log.info("Fetching employment record for employee with email: {}", email);
        EmploymentResponse response = employmentService.getEmploymentByEmployeeEmail(email);
        return ResponseEntity.ok(response);
    }

    /**
     * Update an employment record.
     *
     * @param code the employment code
     * @param request the employment request
     * @return a response with the updated employment record
     */
    @Operation(summary = "Update an employment record", description = "Updates an employment record by its code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employment record updated successfully",
                    content = @Content(schema = @Schema(implementation = EmploymentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Employment record not found")
    })
    @PutMapping("/{code}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<EmploymentResponse> updateEmployment(@PathVariable String code, @Valid @RequestBody EmploymentRequest request) {
        log.info("Updating employment record with code: {}", code);
        EmploymentResponse response = employmentService.updateEmployment(code, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Activate or deactivate an employment record.
     *
     * @param code the employment code
     * @param active true to activate, false to deactivate
     * @return a response with the updated employment record
     */
    @Operation(summary = "Activate or deactivate an employment record", description = "Activates or deactivates an employment record by its code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employment record status updated successfully",
                    content = @Content(schema = @Schema(implementation = EmploymentResponse.class))),
            @ApiResponse(responseCode = "404", description = "Employment record not found")
    })
    @PatchMapping("/{code}/status")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<EmploymentResponse> setEmploymentStatus(@PathVariable String code, @RequestParam boolean active) {
        log.info("Setting employment record status with code: {} to {}", code, active ? "ACTIVE" : "INACTIVE");
        EmploymentResponse response = employmentService.setEmploymentStatus(code, active);
        return ResponseEntity.ok(response);
    }
}