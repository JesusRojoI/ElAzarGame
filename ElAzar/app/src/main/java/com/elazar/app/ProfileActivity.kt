package com.elazar.app

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.elazar.app.databinding.ActivityProfileBinding

/**
 * Vista adicional. Perfil del jugador.
 *
 * En este prototipo los datos se guardan localmente con SharedPreferences.
 * La pantalla alterna entre modo lectura y modo edición para que el flujo de
 * navegación sea completo aun sin un backend.
 */
class ProfileActivity : BaseActivity() {

    private lateinit var binding: ActivityProfileBinding
    private var editing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(binding.toolbar)
        loadProfile()
        setEditing(false)

        binding.buttonEdit.setOnClickListener { setEditing(true) }
        binding.buttonCancelEdit.setOnClickListener {
            loadProfile()
            setEditing(false)
        }
        binding.buttonSave.setOnClickListener { saveProfile() }
        binding.buttonLogout.setOnClickListener { logout() }
    }

    /** Vuelca los datos guardados en la cabecera y en el formulario. */
    private fun loadProfile() {
        binding.textProfileUser.text = "@$user"
        binding.textProfileName.text = displayName
        binding.textProfileBalance.text = money(balance)

        binding.inputName.setText(displayName)
        binding.inputEmail.setText(email)
        binding.inputUser.setText(user)
        binding.inputPhone.setText(phone)
        binding.textMemberSince.text = getString(R.string.profile_session, user)
    }

    /** Activa o desactiva los campos y los botones de edición. */
    private fun setEditing(value: Boolean) {
        editing = value

        listOf(binding.inputName, binding.inputEmail, binding.inputUser, binding.inputPhone)
            .forEach { field ->
                field.isEnabled = value
                field.isFocusable = value
                field.isFocusableInTouchMode = value
                field.isCursorVisible = value
            }

        listOf(binding.layoutName, binding.layoutEmail, binding.layoutUser, binding.layoutPhone)
            .forEach { it.isEnabled = value }

        binding.rowEditActions.visibility = if (value) android.view.View.VISIBLE else android.view.View.GONE
        binding.buttonEdit.visibility = if (value) android.view.View.GONE else android.view.View.VISIBLE
        binding.textEditHint.visibility = if (value) android.view.View.GONE else android.view.View.VISIBLE

        if (value) {
            binding.inputName.requestFocus()
        }
    }

    /** Valida y guarda los cambios del perfil. */
    private fun saveProfile() {
        val name = binding.inputName.text?.toString()?.trim().orEmpty()
        val mail = binding.inputEmail.text?.toString()?.trim().orEmpty()
        val username = binding.inputUser.text?.toString()?.trim()
            ?.removePrefix("@")
            ?.replace(" ", "_")
            .orEmpty()
        val phoneNumber = binding.inputPhone.text?.toString()?.trim().orEmpty()

        if (name.isEmpty()) {
            toast(getString(R.string.profile_error_name)); return
        }
        if (mail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            toast(getString(R.string.profile_error_email)); return
        }
        if (username.isEmpty()) {
            toast(getString(R.string.profile_error_user)); return
        }

        displayName = name
        email = mail
        user = username
        phone = phoneNumber

        loadProfile()
        setEditing(false)
        toast(getString(R.string.profile_saved))
    }

    private fun toast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
