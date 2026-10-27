package com.elazar.app

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.elazar.app.databinding.ActivityLoginBinding

/**
 * Vista 1. Login inicial.
 *
 * El usuario puede crear un nuevo perfil o iniciar sesión con su cuenta de Google,
 * Facebook o con cualquier correo electrónico registrado.
 */
class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener { login() }
        binding.buttonGoogle.setOnClickListener { socialLogin(getString(R.string.login_google)) }
        binding.buttonFacebook.setOnClickListener { socialLogin(getString(R.string.login_facebook)) }
        binding.buttonRegister.setOnClickListener { open(RegisterActivity::class.java) }
        binding.textForgot.setOnClickListener { open(ForgotPasswordActivity::class.java) }
    }

    /** Valida las credenciales y entra al tablero principal. */
    private fun login() {
        val username = binding.inputUser.text?.toString()?.trim().orEmpty()
        val password = binding.inputPassword.text?.toString().orEmpty()

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            toast(getString(R.string.login_error_empty))
            return
        }
        if (password.length < 4) {
            toast(getString(R.string.login_error_invalid))
            return
        }
        user = username.removePrefix("@")
        toast(getString(R.string.login_ok, "@${user}"))
        goToDashboard()
    }

    /**
     * Simula el inicio de sesión con un proveedor externo.
     * En la versión con backend aquí se integrarían las credenciales de Google Sign-In
     * o el SDK de Facebook.
     */
    private fun socialLogin(provider: String) {
        user = DEFAULT_USER
        toast(getString(R.string.login_ok, provider))
        goToDashboard()
    }

    private fun goToDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }

    private fun toast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    /** Oculta el teclado al salir de la pantalla (no hay estado que guardar). */
    override fun onPause() {
        super.onPause()
        currentFocus?.let { view: View ->
            val imm = getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
