package com.example.androidtest.login

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MockAuthRepositoryTest {
    private val repository = MockAuthRepository()

    @Test
    fun acceptsDeterministicTestCredentials() {
        assertTrue(
            repository.login(
                MockAuthRepository.VALID_EMAIL,
                MockAuthRepository.VALID_PASSWORD,
            ),
        )
    }

    @Test
    fun rejectsUnknownEmail() {
        assertFalse(repository.login("other@example.com", MockAuthRepository.VALID_PASSWORD))
    }

    @Test
    fun rejectsWrongPassword() {
        assertFalse(repository.login(MockAuthRepository.VALID_EMAIL, "WrongPass1"))
    }
}
