package com.example.androidtest.login

data class LoginValidationResult(
    val emailError: String? = null,
    val passwordError: String? = null,
) {
    val isValid: Boolean get() = emailError == null && passwordError == null
}

class LoginCredentialsValidator {
    fun validate(email: String, password: String): LoginValidationResult {
        return LoginValidationResult(
            emailError = emailError(email),
            passwordError = passwordError(password),
        )
    }

    private fun emailError(email: String): String? {
        val trimmed = email.trim()
        if (trimmed.isEmpty()) return LoginMessages.EMAIL_REQUIRED
        if (!EMAIL_REGEX.matches(trimmed)) return LoginMessages.EMAIL_INVALID
        return null
    }

    private fun passwordError(password: String): String? {
        if (password.isEmpty()) return LoginMessages.PASSWORD_REQUIRED
        if (password.length < MIN_PASSWORD_LENGTH) return LoginMessages.PASSWORD_TOO_SHORT
        return null
    }

    companion object {
        const val MIN_PASSWORD_LENGTH = 8
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}
