package com.elazar.app

import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.elazar.app.databinding.ActivityRouletteBinding
import kotlin.random.Random

/**
 * Vista adicional. Mesa demostrativa de ruleta.
 *
 * El jugador apuesta a rojo o negro; la ruleta se anima y el saldo se ajusta
 * con el resultado (el 0 siempre gana para la banca).
 */
class RouletteActivity : BaseActivity() {

    private lateinit var binding: ActivityRouletteBinding
    private val lastSpins = mutableListOf<Int>()

    private val redNumbers = setOf(
        1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRouletteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(binding.toolbar)
        updateBalance()
        binding.buttonSpin.setOnClickListener { spin() }
    }

    private fun bet(): Double = when (binding.groupWheelBet.checkedRadioButtonId) {
        R.id.wheelBet25 -> 25.0
        R.id.wheelBet50 -> 50.0
        R.id.wheelBet100 -> 100.0
        else -> 10.0
    }

    private fun updateBalance() {
        binding.textWheelBalance.text = getString(R.string.balance_short, money(balance))
    }

    /** Hace girar la ruleta y resuelve la apuesta al terminar la animación. */
    private fun spin() {
        val stake = bet()
        if (balance < stake) {
            toast(getString(R.string.wheel_no_balance))
            return
        }

        val bettingRed = binding.chipRed.isChecked
        val result = Random.nextInt(0, 37)
        val isRed = result in redNumbers
        val won = result != 0 && (bettingRed == isRed)

        balance -= stake
        updateBalance()

        lastSpins.add(0, result)
        if (lastSpins.size > 6) lastSpins.removeAt(lastSpins.lastIndex)
        binding.textWheelLast.text =
            getString(R.string.wheel_last) + ": " + lastSpins.joinToString(" · ")

        binding.buttonSpin.isEnabled = false
        binding.textWheelResult.text = getString(R.string.wheel_ready)
        binding.textWheelResult.setTextColor(ContextCompat.getColor(this, R.color.white))

        val rotation = 360f * 4 + Random.nextInt(0, 360)
        binding.imageWheel.animate()
            .rotationBy(rotation)
            .setDuration(1400L)
            .withEndAction {
                binding.buttonSpin.isEnabled = true
                showResult(result, isRed, won, stake)
            }
            .start()
    }

    private fun showResult(result: Int, isRed: Boolean, won: Boolean, stake: Double) {
        val color = when {
            result == 0 -> getString(R.string.wheel_bank)
            isRed -> getString(R.string.wheel_red)
            else -> getString(R.string.wheel_black)
        }

        val outcome = when {
            result == 0 -> getString(R.string.wheel_zero)
            won -> {
                balance += stake * 2
                getString(R.string.wheel_win, stake)
            }
            else -> getString(R.string.wheel_lose, stake)
        }

        binding.textWheelResult.text = getString(R.string.wheel_result, result, color) + "\n" + outcome
        val colorRes = if (won) R.color.azar_green else R.color.azar_orange_light
        binding.textWheelResult.setTextColor(ContextCompat.getColor(this, colorRes))
        updateBalance()
    }

    private fun toast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
