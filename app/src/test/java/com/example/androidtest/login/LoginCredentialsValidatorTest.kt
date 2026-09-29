package com.example.androidtest.login

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginCredentialsValidatorTest {
    private val validator = LoginCredentialsValidator()

    @Test
    fun validEmailAndPasswordPass() {
        val result = validator.validate("user@example.com", "Password123")
        assertTrue(result.isValid)
        assertNull(result.emailError)
        assertNull(result.passwordError)
    }

    @Test
    fun emptyEmailIsRequired() {
        val result = validator.validate("   ", "Password123")
        assertFalse(result.isValid)
        assertEquals(LoginMessages.EMAIL_REQUIRED, result.emailError)
    }

    @Test
    fun invalidEmailFormatIsRejected() {
        val result = validator.validate("not-an-email", "Password123")
        assertFalse(result.isValid)
        assertEquals(LoginMessages.EMAIL_INVALID, result.emailError)
    }

    @Test
    fun emptyPasswordIsRequired() {
        val result = validator.validate("user@example.com", "")
        assertFalse(result.isValid)
        assertEquals(LoginMessages.PASSWORD_REQUIRED, result.passwordError)
    }

    @Test
    fun passwordShorterThanEightCharactersIsRejected() {
        val result = validator.validate("user@example.com", "Pass123")
        assertFalse(result.isValid)
        assertEquals(LoginMessages.PASSWORD_TOO_SHORT, result.passwordError)
    }
}
