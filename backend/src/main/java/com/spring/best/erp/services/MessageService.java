package com.spring.best.erp.services;

import com.spring.Best.erp.models.Message;
import com.spring.Best.erp.models.PaySlip;
import com.spring.Best.erp.models.User;
import com.spring.best.erp.repositories.MessageRepository;
import com.spring.best.erp.repositories.PaySlipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

/**
 * Service for managing notification messages.
 *
 * @author Best Backend
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;
    private final PaySlipRepository paySlipRepository;
    private final EmailService emailService;

    /**
     * Generate notification messages for approved payslips.
     *
     * @param month the month
     * @param year the year
     */
    @Transactional
    public void generateMessagesForApprovedPaySlips(Integer month, Integer year) {
        log.info("Generating messages for approved pay slips for month: {} and year: {}", month, year);
        List<PaySlip> approvedPaySlips = paySlipRepository.findByMonthAndYearAndStatus(month, year, "PAID");
        if (approvedPaySlips.isEmpty()) {
            log.warn("No approved pay slips found for month: {} and year: {}", month, year);
            return;
        }

        for (PaySlip paySlip : approvedPaySlips) {
            User employee = paySlip.getEmployee();
            String monthName = Month.of(month).toString();
            String content = generateMessageContent(employee.getFirstName(), monthName, year, "RCA", 
                    paySlip.getNetSalary(), employee.getId().toString());

            Message message = Message.builder()
                    .employee(employee)
                    .content(content)
                    .month(month)
                    .year(year)
                    .createdAt(LocalDateTime.now())
                    .status("PENDING")
                    .build();

            messageRepository.save(message);
            log.info("Generated message for employee: {}, month: {}, year: {}", employee.getEmail(), month, year);
        }
    }

    /**
     * Send notification messages to employees.
     */
    @Transactional
    public void sendPendingMessages() {
        log.info("Sending pending messages");
        List<Message> pendingMessages = messageRepository.findByStatus("PENDING");
        if (pendingMessages.isEmpty()) {
            log.info("No pending messages found");
            return;
        }

        for (Message message : pendingMessages) {
            User employee = message.getEmployee();
            try {
                emailService.sendPayrollNotification(employee.getEmail(), employee.getFullName(), message.getContent());
                message.setStatus("SENT");
                messageRepository.save(message);
                log.info("Sent message to employee: {}", employee.getEmail());
            } catch (Exception e) {
                log.error("Failed to send message to employee: {}", employee.getEmail(), e);
            }
        }
    }

    /**
     * Generate message content according to the specified format.
     *
     * @param firstName the employee's first name
     * @param month the month name
     * @param year the year
     * @param institution the institution name
     * @param amount the salary amount
     * @param employeeId the employee ID
     * @return the formatted message content
     */
    private String generateMessageContent(String firstName, String month, Integer year, 
                                         String institution, Double amount, String employeeId) {
        return String.format("Dear %s, Your salary of %s/%d from %s %.2f has been credited to your %s account successfully.",
                firstName, month, year, institution, amount, employeeId);
    }
}