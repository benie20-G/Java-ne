package com.spring.Best.erp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity class representing notification messages in the ERP system.
 * <p>
 * This class stores messages that are generated when payroll is approved
 * and need to be sent to employees.
 * </p>
 *
 * @author Best Backend
 * @version 1.0
 * @since 1.0
 */
@Data
@Entity
@Table(name = "messages")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
    /**
     * The unique identifier for the message.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The employee to whom the message is addressed.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private User employee;

    /**
     * The content of the message.
     */
    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    /**
     * The month for which the payroll message is generated.
     */
    @Column(name = "month", nullable = false)
    private Integer month;

    /**
     * The year for which the payroll message is generated.
     */
    @Column(name = "year", nullable = false)
    private Integer year;

    /**
     * The timestamp when the message was created.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * The status of the message (sent or pending).
     */
    @Column(name = "status", nullable = false)
    private String status = "PENDING";
}