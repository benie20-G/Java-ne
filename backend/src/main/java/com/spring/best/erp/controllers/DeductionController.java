package com.spring.best.erp.controllers;

import com.spring.Best.erp.dtos.request.DeductionRequest;
import com.spring.Best.erp.dtos.response.DeductionResponse;
import com.spring.best.erp.services.DeductionService;
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
 * Controller for managing deductions.
 *
 * @author Best Backend
 * @since 1.0
 */
@Tag(name = "Deductions", description = "Endpoints for managing deductions")
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/deductions")
public class DeductionController {

    private final DeductionService deductionService;

    /**
     * Create a new deduction.
     *
     * @param request the deduction request
     * @return a response with the created deduction
     */
    @Operation(summary = "Create a new deduction", description = "Creates a new deduction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deduction created successfully",
                    content = @Content(schema = @Schema(implementation = DeductionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DeductionResponse> createDeduction(@Valid @RequestBody DeductionRequest request) {
        log.info("Creating deduction with name: {}", request.name());
        DeductionResponse response = deductionService.createDeduction(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all deductions.
     *
     * @return a response with all deductions
     */
    @Operation(summary = "Get all deductions", description = "Retrieves all deductions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deductions retrieved successfully",
                    content = @Content(schema = @Schema(implementation = DeductionResponse.class)))
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<List<DeductionResponse>> getAllDeductions() {
        log.info("Fetching all deductions");
        List<DeductionResponse> responses = deductionService.getAllDeductions();
        return ResponseEntity.ok(responses);
    }

    /**
     * Get a deduction by code.
     *
     * @param code the deduction code
     * @return a response with the deduction
     */
    @Operation(summary = "Get a deduction by code", description = "Retrieves a deduction by its code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deduction retrieved successfully",
                    content = @Content(schema = @Schema(implementation = DeductionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Deduction not found")
    })
    @GetMapping("/{code}")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<DeductionResponse> getDeductionByCode(@PathVariable String code) {
        log.info("Fetching deduction with code: {}", code);
        DeductionResponse response = deductionService.getDeductionByCode(code);
        return ResponseEntity.ok(response);
    }

    /**
     * Get a deduction by name.
     *
     * @param name the deduction name
     * @return a response with the deduction
     */
    @Operation(summary = "Get a deduction by name", description = "Retrieves a deduction by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deduction retrieved successfully",
                    content = @Content(schema = @Schema(implementation = DeductionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Deduction not found")
    })
    @GetMapping("/name/{name}")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<DeductionResponse> getDeductionByName(@PathVariable String name) {
        log.info("Fetching deduction with name: {}", name);
        DeductionResponse response = deductionService.getDeductionByName(name);
        return ResponseEntity.ok(response);
    }

    /**
     * Update a deduction.
     *
     * @param code the deduction code
     * @param request the deduction request
     * @return a response with the updated deduction
     */
    @Operation(summary = "Update a deduction", description = "Updates a deduction by its code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deduction updated successfully",
                    content = @Content(schema = @Schema(implementation = DeductionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Deduction not found")
    })
    @PutMapping("/{code}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DeductionResponse> updateDeduction(@PathVariable String code, @Valid @RequestBody DeductionRequest request) {
        log.info("Updating deduction with code: {}", code);
        DeductionResponse response = deductionService.updateDeduction(code, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a deduction.
     *
     * @param code the deduction code
     * @return a response indicating success
     */
    @Operation(summary = "Delete a deduction", description = "Deletes a deduction by its code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deduction deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Deduction not found")
    })
    @DeleteMapping("/{code}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteDeduction(@PathVariable String code) {
        log.info("Deleting deduction with code: {}", code);
        deductionService.deleteDeduction(code);
        return ResponseEntity.ok("Deduction deleted successfully");
    }

    /**
     * Initialize default deductions.
     *
     * @return a response indicating success
     */
    @Operation(summary = "Initialize default deductions", description = "Initializes default deductions if they don't exist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Default deductions initialized successfully")
    })
    @PostMapping("/initialize")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> initializeDefaultDeductions() {
        log.info("Initializing default deductions");
        deductionService.initializeDefaultDeductions();
        return ResponseEntity.ok("Default deductions initialized successfully");
    }
}