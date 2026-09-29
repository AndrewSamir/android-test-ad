package com.example.androidtest.login

class LoginController(
    private val validator: LoginCredentialsValidator = LoginCredentialsValidator(),
    private val authRepository: AuthRepository = MockAuthRepository(),
) {
    var state: LoginViewState = LoginViewState()
        private set

    var onStateChanged: ((LoginViewState) -> Unit)? = null

    fun submit(email: String, password: String) {
        if (state.isLoading) return

        val validation = validator.validate(email, password)
        if (!validation.isValid) {
            update(
                state.copy(
                    isLoading = false,
                    emailError = validation.emailError,
                    passwordError = validation.passwordError,
                    errorMessage = null,
                    navigateToHome = false,
                ),
            )
            return
        }

        update(
            state.copy(
                isLoading = true,
                emailError = null,
                passwordError = null,
                errorMessage = null,
                navigateToHome = false,
            ),
        )

        val authenticated = authRepository.login(email.trim(), password)
        if (authenticated) {
            update(
                state.copy(
                    isLoading = true,
                    errorMessage = null,
                    navigateToHome = true,
                ),
            )
        } else {
            update(
                state.copy(
                    isLoading = false,
                    errorMessage = LoginMessages.INVALID_CREDENTIALS,
                    navigateToHome = false,
                ),
            )
        }
    }

    private fun update(newState: LoginViewState) {
        state = newState
        onStateChanged?.invoke(state)
    }
}
