package com.spring.Best.erp.dtos.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for pay slip generation request.
 *
 * @param month the month for which the pay slip is generated (1-12)
 * @param year the year for which the pay slip is generated
 *
 * @author Best Backend
 * @since 1.0
 */
public record PaySlipRequest(
        @NotNull(message = "Month is required")
        @Min(value = 1, message = "Month must be between 1 and 12")
        @Max(value = 12, message = "Month must be between 1 and 12")
        Integer month,

        @NotNull(message = "Year is required")
        @Min(value = 2000, message = "Year must be 2000 or later")
        Integer year
) {
}