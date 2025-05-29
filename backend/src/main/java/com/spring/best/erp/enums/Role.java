package com.spring.Best.erp.enums;

/**
 * Enum representing user roles in the ERP system.
 *
 * @author Best Backend
 * @since 1.0
 */
public enum Role {
    /**
     * Administrator role with elevated privileges.
     * Can approve salary payments.
     */
    ROLE_ADMIN,

    /**
     * Manager role with privileges to process salary and add employee details.
     */
    ROLE_MANAGER,

    /**
     * Employee role with basic access to view personal details and pay slips.
     */
    ROLE_EMPLOYEE
}
