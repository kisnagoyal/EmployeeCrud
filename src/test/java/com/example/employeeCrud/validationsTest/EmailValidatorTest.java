package com.example.employeeCrud.validationsTest;

import com.example.employeeCrud.validations.EmailValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailValidatorTest {

    private final EmailValidator validator = new EmailValidator();

    @Test
    void testValidEmail() {
        assertTrue(validator.isValid("test@example.com", null));
    }

    @Test
    void testInvalidEmail() {
        assertFalse(validator.isValid("invalid-email", null));
    }

    @Test
    void testNullEmail() {
        assertFalse(validator.isValid(null, null));
    }
}
