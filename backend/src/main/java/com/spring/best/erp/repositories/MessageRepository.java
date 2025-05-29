package com.spring.best.erp.repositories;

import com.spring.Best.erp.models.Message;
import com.spring.Best.erp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Message entities.
 *
 * @author Best Backend
 * @since 1.0
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    /**
     * Find all messages for a specific employee.
     *
     * @param employee the employee
     * @return a list of messages for the employee
     */
    List<Message> findByEmployee(User employee);

    /**
     * Find all messages for a specific month and year.
     *
     * @param month the month
     * @param year the year
     * @return a list of messages for the month and year
     */
    List<Message> findByMonthAndYear(Integer month, Integer year);

    /**
     * Find all messages for a specific employee, month, and year.
     *
     * @param employee the employee
     * @param month the month
     * @param year the year
     * @return a list of messages for the employee, month, and year
     */
    List<Message> findByEmployeeAndMonthAndYear(User employee, Integer month, Integer year);

    /**
     * Find all messages with a specific status.
     *
     * @param status the status
     * @return a list of messages with the status
     */
    List<Message> findByStatus(String status);

    /**
     * Find all messages for a specific employee with a specific status.
     *
     * @param employee the employee
     * @param status the status
     * @return a list of messages for the employee with the status
     */
    List<Message> findByEmployeeAndStatus(User employee, String status);
}