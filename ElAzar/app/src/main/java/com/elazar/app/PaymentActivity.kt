package com.elazar.app

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.elazar.app.databinding.ActivityPaymentBinding

/**
 * Vista 5. Alta de medio de pago.
 *
 * Permite registrar los datos de tarjeta para recibir recompensas adicionales.
 * La vista previa de la tarjeta se actualiza conforme el usuario escribe.
 */
class PaymentActivity : BaseActivity() {

    private lateinit var binding: ActivityPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(binding.toolbar)
        binding.buttonCancel.setOnClickListener { finish() }
        linkPreview()
    }

    /** Enlaza los campos del formulario con la vista previa de la tarjeta. */
    private fun linkPreview() {
        binding.inputHolder.addTextChangedListener(simpleWatcher {
            binding.textCardHolder.text = it.uppercase().ifEmpty { getString(R.string.payment_hint_holder) }
        })
        binding.inputCard.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) = Unit
            override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) = Unit
            override fun afterTextChanged(s: Editable?) {
                val digits = s?.toString()?.filter { it.isDigit() }.orEmpty().take(16)
                binding.textCardNumber.text = formatCard(digits)
            }
        })
        binding.inputExpiry.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) = Unit
            override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) = Unit
            override fun afterTextChanged(s: Editable?) {
                val digits = s?.toString()?.filter { it.isDigit() }.orEmpty().take(4)
                val pretty = if (digits.length > 2) "${digits.take(2)}/${digits.drop(2)}" else digits
                if (pretty != s?.toString()) {
                    binding.inputExpiry.setText(pretty)
                    binding.inputExpiry.setSelection(pretty.length)
                }
                binding.textCardExpiry.text = pretty.ifEmpty { "MM/AA" }
            }
        })
    }

    private fun simpleWatcher(action: (String) -> Unit) = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) = Unit
        override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) = Unit
        override fun afterTextChanged(s: Editable?) = action(s?.toString().orEmpty())
    }

    /** Agrupa el número de tarjeta en bloques de cuatro dígitos. */
    private fun formatCard(digits: String): String {
        if (digits.isEmpty()) return "•••• •••• •••• ••••"
        val padded = digits.padEnd(16, '•')
        return padded.chunked(4).joinToString(" ")
    }

    /**
     * Acción del botón Confirmar (declarada con android:onClick en el layout).
     * Valida los datos y simula el alta del medio de pago.
     */
    fun onConfirmPayment(view: View) {
        val holder = binding.inputHolder.text?.toString()?.trim().orEmpty()
        val number = binding.inputCard.text?.toString()?.filter { it.isDigit() }.orEmpty()
        val expiry = binding.inputExpiry.text?.toString().orEmpty()
        val cvv = binding.inputCvv.text?.toString().orEmpty()

        if (holder.isEmpty()) {
            toast(getString(R.string.payment_error_holder)); return
        }
        if (number.length != 16) {
            toast(getString(R.string.payment_error_card)); return
        }
        if (expiry.length != 5) {
            toast(getString(R.string.payment_error_expiry)); return
        }
        if (cvv.length != 3) {
            toast(getString(R.string.payment_error_cvv)); return
        }

        val default = if (binding.checkDefault.isChecked) " (predeterminada)" else ""
        toast(getString(R.string.payment_ok, number.takeLast(4)) + default)
        finish()
    }

    private fun toast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
