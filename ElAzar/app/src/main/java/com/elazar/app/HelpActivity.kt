package com.elazar.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.elazar.app.databinding.ActivityHelpBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Vista adicional. Centro de ayuda, preguntas frecuentes y contacto.
 */
class HelpActivity : BaseActivity() {

    private lateinit var binding: ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(binding.toolbar)

        binding.buttonContact.setOnClickListener { sendSupportEmail() }
        binding.buttonPrivacy.setOnClickListener {
            showDialog(R.string.dialog_privacy_title, R.string.dialog_privacy_body)
        }
        binding.buttonResponsible.setOnClickListener {
            showDialog(R.string.dialog_responsible_title, R.string.dialog_responsible_body)
        }
    }

    /** Abre el cliente de correo del dispositivo con el destinatario de soporte. */
    private fun sendSupportEmail() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:soporte@elazar.mx")
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.help_mail_subject))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.help_contact_body))
        }
        runCatching { startActivity(intent) }.onFailure {
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.help_contact)
                .setMessage(R.string.help_contact_body)
                .setPositiveButton(R.string.dialog_ok, null)
                .show()
        }
    }
}
