package com.example.androidtest.login

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginControllerTest {
    @Test
    fun validCredentialsShowLoadingAndNavigateHome() {
        val controller = LoginController()
        val observed = mutableListOf<LoginViewState>()
        controller.onStateChanged = { observed.add(it) }

        controller.submit(MockAuthRepository.VALID_EMAIL, MockAuthRepository.VALID_PASSWORD)

        assertTrue(observed.any { it.isLoading && !it.navigateToHome })
        assertTrue(controller.state.isLoading)
        assertTrue(controller.state.navigateToHome)
        assertNull(controller.state.errorMessage)
    }

    @Test
    fun invalidCredentialsStayOnLoginWithExactError() {
        val controller = LoginController()

        controller.submit("user@example.com", "WrongPass1")

        assertFalse(controller.state.navigateToHome)
        assertFalse(controller.state.isLoading)
        assertEquals(LoginMessages.INVALID_CREDENTIALS, controller.state.errorMessage)
    }

    @Test
    fun invalidEmailDoesNotAuthenticate() {
        val auth = CountingAuthRepository()
        val controller = LoginController(authRepository = auth)

        controller.submit("not-an-email", "Password123")

        assertEquals(0, auth.calls)
        assertFalse(controller.state.navigateToHome)
        assertEquals(LoginMessages.EMAIL_INVALID, controller.state.emailError)
        assertNull(controller.state.errorMessage)
    }

    @Test
    fun shortPasswordDoesNotAuthenticate() {
        val auth = CountingAuthRepository()
        val controller = LoginController(authRepository = auth)

        controller.submit("user@example.com", "short")

        assertEquals(0, auth.calls)
        assertEquals(LoginMessages.PASSWORD_TOO_SHORT, controller.state.passwordError)
    }

    @Test
    fun duplicateSubmitWhileLoadingIsIgnored() {
        val auth = CountingAuthRepository(result = true)
        val controller = LoginController(authRepository = auth)

        controller.submit(MockAuthRepository.VALID_EMAIL, MockAuthRepository.VALID_PASSWORD)
        controller.submit(MockAuthRepository.VALID_EMAIL, MockAuthRepository.VALID_PASSWORD)

        assertEquals(1, auth.calls)
        assertTrue(controller.state.navigateToHome)
    }

    @Test
    fun failedLoginAllowsRetry() {
        val auth = CountingAuthRepository(result = false)
        val controller = LoginController(authRepository = auth)

        controller.submit(MockAuthRepository.VALID_EMAIL, MockAuthRepository.VALID_PASSWORD)
        controller.submit(MockAuthRepository.VALID_EMAIL, MockAuthRepository.VALID_PASSWORD)

        assertEquals(2, auth.calls)
        assertEquals(LoginMessages.INVALID_CREDENTIALS, controller.state.errorMessage)
    }

    private class CountingAuthRepository(
        private val result: Boolean = false,
    ) : AuthRepository {
        var calls: Int = 0
            private set

        override fun login(email: String, password: String): Boolean {
            calls += 1
            return result
        }
    }
}
