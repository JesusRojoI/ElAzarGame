package com.elazar.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Actividad base de El Azar.
 *
 * Centraliza lo que comparten todas las pantallas:
 *  - el menú de opciones (Práctica 8: "menús"),
 *  - el estado simple de sesión (usuario y saldo) guardado en SharedPreferences,
 *  - la navegación entre actividades,
 *  - los diálogos de información legal.
 */
open class BaseActivity : AppCompatActivity() {

    companion object {
        const val PREFS = "elazar_prefs"
        const val KEY_USER = "user"
        const val KEY_NAME = "name"
        const val KEY_EMAIL = "email"
        const val KEY_PHONE = "phone"
        const val KEY_BALANCE = "balance"
        const val DEFAULT_USER = "usuario.G"
        const val DEFAULT_NAME = "Jugador de El Azar"
        const val DEFAULT_EMAIL = "usuario@elazar.mx"
        const val DEFAULT_PHONE = ""
        const val DEFAULT_BALANCE = 1250.0

        /** Caras de dado en Unicode, usadas por las mesas de juego. */
        val DICE_FACES = arrayOf("⚀", "⚁", "⚂", "⚃", "⚄", "⚅")
    }

    protected val prefs by lazy {
        getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    }

    /** Nombre del jugador en sesión. */
    var user: String
        get() = prefs.getString(KEY_USER, DEFAULT_USER) ?: DEFAULT_USER
        set(value) {
            prefs.edit().putString(KEY_USER, value).apply()
        }

    /** Nombre completo mostrado en el perfil. */
    var displayName: String
        get() = prefs.getString(KEY_NAME, DEFAULT_NAME) ?: DEFAULT_NAME
        set(value) {
            prefs.edit().putString(KEY_NAME, value).apply()
        }

    /** Correo electrónico del jugador. */
    var email: String
        get() = prefs.getString(KEY_EMAIL, DEFAULT_EMAIL) ?: DEFAULT_EMAIL
        set(value) {
            prefs.edit().putString(KEY_EMAIL, value).apply()
        }

    /** Teléfono opcional del jugador. */
    var phone: String
        get() = prefs.getString(KEY_PHONE, DEFAULT_PHONE) ?: DEFAULT_PHONE
        set(value) {
            prefs.edit().putString(KEY_PHONE, value).apply()
        }

    /** Saldo disponible del jugador. */
    var balance: Double
        get() = prefs.getLong("balance_cents", (DEFAULT_BALANCE * 100).toLong()) / 100.0
        set(value) {
            prefs.edit().putLong("balance_cents", (value * 100).toLong()).apply()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Evita que el teclado se abra solo al entrar a una pantalla con campos.
        window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
        )
    }

    /**
     * Da el foco inicial al contenedor raíz en lugar de al primer campo o lista.
     * Así el teclado no aparece solo y las listas no desplazan la cabecera.
     */
    override fun setContentView(view: View?) {
        super.setContentView(view)
        view?.isFocusableInTouchMode = true
        view?.requestFocus()
    }

    /** Configura la barra superior de la pantalla conservando el título del layout. */
    protected fun setupToolbar(toolbar: MaterialToolbar, showUp: Boolean = true) {
        val layoutTitle = toolbar.title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(showUp)
        // setSupportActionBar reemplaza el título con la etiqueta de la actividad;
        // lo restauramos con el que declaramos en el XML de la vista.
        if (layoutTitle != null) supportActionBar?.title = layoutTitle
    }

    /** Formatea un monto con el símbolo de peso. */
    protected fun money(amount: Double): String = getString(R.string.dashboard_balance_value, amount)

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.action_profile -> {
                open(ProfileActivity::class.java)
                return true
            }
            R.id.action_help -> {
                open(HelpActivity::class.java)
                return true
            }
            R.id.action_store -> {
                open(StoreActivity::class.java)
                return true
            }
            R.id.action_payment -> {
                open(PaymentActivity::class.java)
                return true
            }
            R.id.action_privacy -> {
                showDialog(R.string.dialog_privacy_title, R.string.dialog_privacy_body)
                return true
            }
            R.id.action_responsible -> {
                showDialog(R.string.dialog_responsible_title, R.string.dialog_responsible_body)
                return true
            }
            R.id.action_info -> {
                showDialog(R.string.dialog_info_title, R.string.dialog_info_body)
                return true
            }
            R.id.action_share -> {
                share()
                return true
            }
            R.id.action_logout -> {
                logout()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    protected fun open(target: Class<*>) {
        startActivity(Intent(this, target))
    }

    protected fun showDialog(titleRes: Int, messageRes: Int) {
        MaterialAlertDialogBuilder(this)
            .setTitle(titleRes)
            .setMessage(messageRes)
            .setPositiveButton(R.string.dialog_ok, null)
            .show()
    }

    protected fun share() {
        val send = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text))
        }
        startActivity(Intent.createChooser(send, getString(R.string.menu_share)))
    }

    protected fun logout() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.menu_logout)
            .setMessage(R.string.login_legal)
            .setNegativeButton(R.string.payment_cancel, null)
            .setPositiveButton(R.string.menu_logout) { _, _ ->
                val intent = Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                finish()
            }
            .show()
    }
}
