package com.elazar.app

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.elazar.app.databinding.ActivityForgotPasswordBinding

/**
 * Vista adicional. Recuperación de contraseña.
 *
 * Simula el envío de un enlace de restablecimiento; no hay backend todavía.
 */
class ForgotPasswordActivity : BaseActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(binding.toolbar)
        binding.buttonBackLogin.setOnClickListener { finish() }
        binding.buttonSendLink.setOnClickListener { sendLink() }
    }

    private fun sendLink() {
        val mail = binding.inputRecoveryEmail.text?.toString()?.trim().orEmpty()
        if (mail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            Toast.makeText(this, getString(R.string.forgot_error), Toast.LENGTH_SHORT).show()
            return
        }
        Toast.makeText(this, getString(R.string.forgot_sent), Toast.LENGTH_LONG).show()
        finish()
    }
}
