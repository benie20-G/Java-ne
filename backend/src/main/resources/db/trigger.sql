-- Trigger to generate messages when payslips are approved
DELIMITER //

CREATE TRIGGER after_payslip_update
AFTER UPDATE ON pay_slips
FOR EACH ROW
BEGIN
    -- Check if status changed from PENDING to PAID
    IF OLD.status = 'PENDING' AND NEW.status = 'PAID' THEN
        -- Insert a message for the employee
        INSERT INTO messages (employee_id, content, month, year, created_at, status)
        SELECT 
            NEW.employee_id,
            CONCAT('Dear ', u.first_name, ', Your salary of ', MONTHNAME(STR_TO_DATE(NEW.month, '%m')), '/', NEW.year, 
                   ' from RCA ', FORMAT(NEW.net_salary, 2), ' has been credited to your ', u.id, ' account successfully.'),
            NEW.month,
            NEW.year,
            NOW(),
            'PENDING'
        FROM users u
        WHERE u.id = NEW.employee_id;
    END IF;
END //

DELIMITER ;