package com.example.androidtest.login

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidtest.MainActivity
import com.example.androidtest.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    private val controller = LoginController()
    private var passwordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginScreen = findViewById<View>(R.id.login_screen)
        ViewCompat.setOnApplyWindowInsetsListener(loginScreen) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val emailInput = findViewById<TextInputEditText>(R.id.email_input)
        val passwordInput = findViewById<TextInputEditText>(R.id.password_input)
        val emailLayout = findViewById<TextInputLayout>(R.id.email_input_layout)
        val passwordLayout = findViewById<TextInputLayout>(R.id.password_input_layout)
        val errorMessage = findViewById<TextView>(R.id.error_message)
        val loginButton = findViewById<MaterialButton>(R.id.login_button)
        val loading = findViewById<ProgressBar>(R.id.login_loading)
        val visibilityToggle = findViewById<ImageButton>(R.id.password_visibility_toggle)

        controller.onStateChanged = { state ->
            emailLayout.error = state.emailError
            passwordLayout.error = state.passwordError
            if (state.errorMessage.isNullOrEmpty()) {
                errorMessage.visibility = View.GONE
                errorMessage.text = ""
            } else {
                errorMessage.visibility = View.VISIBLE
                errorMessage.text = state.errorMessage
            }
            loginButton.isEnabled = !state.isLoading
            loading.visibility = if (state.isLoading) View.VISIBLE else View.GONE
            if (state.navigateToHome) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        loginButton.setOnClickListener {
            controller.submit(
                emailInput.text?.toString().orEmpty(),
                passwordInput.text?.toString().orEmpty(),
            )
        }

        visibilityToggle.setOnClickListener {
            passwordVisible = !passwordVisible
            val selection = passwordInput.selectionEnd
            passwordInput.inputType = if (passwordVisible) {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            passwordInput.setSelection(selection.coerceAtLeast(0))
            visibilityToggle.contentDescription = getString(
                if (passwordVisible) {
                    R.string.hide_password
                } else {
                    R.string.show_password
                },
            )
        }
    }
}
