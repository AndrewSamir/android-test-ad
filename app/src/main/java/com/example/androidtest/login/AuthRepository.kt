package com.example.androidtest.login

interface AuthRepository {
    fun login(email: String, password: String): Boolean
}

class MockAuthRepository : AuthRepository {
    override fun login(email: String, password: String): Boolean {
        return email == VALID_EMAIL && password == VALID_PASSWORD
    }

    companion object {
        const val VALID_EMAIL = "user@example.com"
        const val VALID_PASSWORD = "Password123"
    }
}
