package com.spring.best.erp.services;

import com.spring.Best.erp.dtos.request.AccountActivationRequest;
import com.spring.Best.erp.dtos.request.ResetPasswordRequest;
import com.spring.Best.erp.dtos.response.VerificationResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Service for sending email notifications in the vehicle tracking system.
 * Handles password reset, account activation, verification, and transfer notifications.
 *
 * @author Best Backend
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${reset-password-url}")
    private String resetPasswordUrl;

    @Value("${support-email}")
    private String supportEmail;

    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * Generates a common email signature with support contact and copyright.
     *
     * @return the HTML signature
     */
    private String getCommonSignature() {
        return "<br><br>If you need help, contact us at: <a href='mailto:" + supportEmail + "'>" + supportEmail + "</a><br>© " + LocalDate.now().getYear();
    }

    /**
     * Sends a password reset email with a reset code and link.
     *
     * @param request the reset password request
     * @throws IllegalStateException if email sending fails
     */
    public void sendResetPasswordMail(ResetPasswordRequest request) {

    }

    /**
     * Sends an account activation email with a verification code.
     *
     * @param request the account activation request
     * @throws IllegalStateException if email sending fails
     */
    public void sendActivateAccountEmail(AccountActivationRequest request) {
        log.info("Sending account activation email to: {}", request.email());
        String subject = "Account Activation Request";
        String html = "<p>Hello " + request.fullName() + ",</p>"
                + "<p>Please use the following code to activate your account:</p>"
                + "<h2>" + request.verificationCode() + "</h2>"
                + "<p><strong>This code will expire at: " + request.expiresAt() + "</strong></p>"
                + "<p>If your code expires, you can request a new one from the activation page.</p>"
                + getCommonSignature();
        sendEmail(request.email(), subject, html);
    }

    /**
     * Sends a confirmation email after successful account verification.
     *
     * @param response the verification response
     * @throws IllegalStateException if email sending fails
     */
    public void sendAccountVerifiedSuccessfullyEmail(VerificationResponse response) {
        log.info("Sending account verification success email to: {}", response.email());
        String subject = "Account Verification Successful";
        String html = "<p>Hi " + response.fullName() + ",</p>"
                + "<p>Your account has been verified successfully. Welcome aboard!</p>"
                + "<p>You can now log in to your account and start using our vehicle tracking system.</p>"
                + getCommonSignature();
        sendEmail(response.email(), subject, html);
    }

    /**
     * Sends a confirmation email after successful password reset.
     *
     * @param response the verification response
     * @throws IllegalStateException if email sending fails
     */
    public void sendPasswordResetSuccessfully(VerificationResponse response) {
        log.info("Sending password reset success email to: {}", response.email());
        String subject = "Password Reset Successful";
        String html = "<p>Hello " + response.fullName() + ",</p>"
                + "<p>Your password has been reset successfully.</p>"
                + getCommonSignature();
        sendEmail(response.email(), subject, html);
    }

    /**
     * Sends a notification email when a verification code expires.
     *
     * @param email     the recipient's email
     * @param fullName  the recipient's full name
     * @throws IllegalStateException if email sending fails
     */
    public void sendOtpExpiredNotification(String email, String fullName) {
        log.info("Sending OTP expired notification to: {}", email);
        String subject = "Verification Code Expired";
        String html = "<p>Hello " + fullName + ",</p>"
                + "<p>Your verification code has expired.</p>"
                + "<p>Please request a new code by visiting the activation page.</p>"
                + getCommonSignature();
        sendEmail(email, subject, html);
    }

    /**
     * Sends a payroll notification to an employee.
     *
     * @param email the employee's email
     * @param fullName the employee's full name
     * @param message the notification message
     * @throws IllegalStateException if email sending fails
     */
    public void sendPayrollNotification(String email, String fullName, String message) {
        log.info("Sending payroll notification to: {}", email);
        String subject = "Salary Payment Notification";
        String html = "<p>Hello " + fullName + ",</p>"
                + "<p>" + message + "</p>"
                + "<p>Thank you for your continued service.</p>"
                + getCommonSignature();
        sendEmail(email, subject, html);
    }

    /**
     * Sends an HTML email to the specified recipient.
     *
     * @param to           the recipient's email address
     * @param subject      the email subject
     * @param htmlContent  the HTML content of the email
     * @throws IllegalStateException if email sending fails
     */
    private void sendEmail(String to, String subject, String htmlContent) {
        if (to == null || to.isBlank()) {
            log.error("Cannot send email: recipient address is null or empty");
            throw new IllegalStateException("Recipient email address is required");
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(message);
            log.info("Successfully sent email to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage(), e);
            throw new IllegalStateException("Failed to send email: " + e.getMessage(), e);
        }
    }
}
