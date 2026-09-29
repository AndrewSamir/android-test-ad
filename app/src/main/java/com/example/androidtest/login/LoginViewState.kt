package com.example.androidtest.login

data class LoginViewState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val navigateToHome: Boolean = false,
)
