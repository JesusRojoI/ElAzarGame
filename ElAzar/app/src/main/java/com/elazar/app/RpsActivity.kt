package com.elazar.app

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.elazar.app.databinding.ActivityRpsBinding

/**
 * Vista 9. Juego "Piedra, papel o tijera" con apuesta.
 *
 * Se practica el RadioGroup (jugada y monto de la apuesta), el CheckBox de la opción
 * "Todo o nada" y el TextView de resultado. El saldo se actualiza con cada ronda.
 */
class RpsActivity : BaseActivity() {

    private lateinit var binding: ActivityRpsBinding
    private var wins = 0
    private var losses = 0
    private var round = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRpsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(binding.toolbar)

        binding.textTurn.text = getString(R.string.rps_turn, "@u2")
        binding.textRound.text = getString(R.string.rps_round, round)
        binding.groupBet.setOnCheckedChangeListener { _, _ -> updateBet() }
        updateBet()
    }

    private fun bet(): Double = when (binding.groupBet.checkedRadioButtonId) {
        R.id.radioBet25 -> 25.0
        R.id.radioBet50 -> 50.0
        R.id.radioBet100 -> 100.0
        else -> 10.0
    }

    private fun updateBet() {
        binding.textBet.text = getString(R.string.rps_bet_value, bet())
    }

    /** Acción de los botones de apuesta (android:onClick). */
    fun onBetChanged(view: View) = updateBet()

    /**
     * Acción del botón Jugar ronda (android:onClick en activity_rps.xml).
     */
    fun onPlayClicked(view: View) {
        val playerMove = when (binding.groupGesture.checkedRadioButtonId) {
            R.id.radioRock -> "rock"
            R.id.radioPaper -> "paper"
            R.id.radioScissors -> "scissors"
            else -> null
        }

        if (playerMove == null) {
            toast(getString(R.string.rps_choose_error))
            return
        }

        val opponentMove = listOf("rock", "paper", "scissors").random()
        binding.textPlayerMove.text = emoji(playerMove)
        binding.textOpponentMove.text = emoji(opponentMove)

        val draw = playerMove == opponentMove
        val playerWins = when (playerMove) {
            "rock" -> opponentMove == "scissors"
            "paper" -> opponentMove == "rock"
            else -> opponentMove == "paper"
        }

        // La opción "Todo o nada" duplica la apuesta de la ronda.
        val factor = if (binding.checkAllOrNothing.isChecked) 2 else 1
        val stake = bet() * factor

        binding.textTiebreak.isVisible = draw

        when {
            draw -> {
                binding.textResult.text = getString(R.string.rps_draw)
            }
            playerWins -> {
                wins++
                balance += stake
                binding.textResult.text = getString(R.string.rps_win, stake)
            }
            else -> {
                losses++
                balance -= stake
                binding.textResult.text = getString(R.string.rps_lose, stake)
            }
        }

        binding.textScore.text = "$wins - $losses"
        round++
        binding.textRound.text = getString(R.string.rps_round, round)
    }

    private fun emoji(move: String): String = when (move) {
        "rock" -> getString(R.string.rps_gesture_rock)
        "paper" -> getString(R.string.rps_gesture_paper)
        else -> getString(R.string.rps_gesture_scissors)
    }

    private fun toast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
