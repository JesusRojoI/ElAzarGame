package com.elazar.app

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.elazar.app.databinding.ActivityRegisterBinding

/**
 * Vista 2. Crear cuenta.
 *
 * Se aplican los CheckBox (términos y condiciones) y la validación de los campos
 * de texto introducidos en la interfaz.
 */
class RegisterActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonRegister.setOnClickListener { register() }
        binding.buttonGoLogin.setOnClickListener { finish() }
        binding.imageBack.setOnClickListener { finish() }
    }

    private fun register() {
        val name = binding.inputName.text?.toString()?.trim().orEmpty()
        val email = binding.inputEmail.text?.toString()?.trim().orEmpty()
        val username = binding.inputUser.text?.toString()?.trim().orEmpty()
        val password = binding.inputPassword.text?.toString().orEmpty()
        val confirm = binding.inputConfirm.text?.toString().orEmpty()

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) ||
            TextUtils.isEmpty(username) || TextUtils.isEmpty(password)
        ) {
            toast(getString(R.string.register_error_fields))
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            toast(getString(R.string.register_error_email))
            return
        }
        if (password.length < 8) {
            toast(getString(R.string.register_error_password_length))
            return
        }
        if (password != confirm) {
            toast(getString(R.string.register_error_password))
            return
        }
        if (!binding.checkTerms.isChecked) {
            toast(getString(R.string.register_error_terms))
            return
        }

        // Alta simulada: guardamos el perfil en las preferencias del dispositivo.
        displayName = name
        this.email = email
        user = username.removePrefix("@")
        toast(getString(R.string.register_ok))
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }

    private fun toast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
