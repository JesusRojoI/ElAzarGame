package com.elazar.app

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.elazar.app.databinding.ActivityDiceBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.random.Random

/**
 * Vista 7. Juego "Dados M".
 *
 * Se practica el RadioGroup (cuántos dados lanzar), el Button con android:onClick y
 * los TextView de resultado. La apuesta crece según el número de dados y el saldo del
 * jugador se actualiza al final de cada ronda.
 */
class DiceGameActivity : BaseActivity() {

    private lateinit var binding: ActivityDiceBinding
    private var rolls = 0
    private var skin = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(binding.toolbar)

        binding.groupDiceCount.setOnCheckedChangeListener { _, _ -> updateDiceVisibility() }
        binding.buttonSkins.setOnClickListener { chooseSkin() }
        binding.buttonRooms.setOnClickListener { chooseRoom() }
        binding.buttonOneVsOne.setOnClickListener { toast(getString(R.string.dice_1v1)) }
        binding.buttonOneVsPc.setOnClickListener { toast(getString(R.string.dice_1vpc)) }

        binding.textRolls.text = getString(R.string.dice_roll_count, rolls)
        updateDiceVisibility()
    }

    /** Muestra u oculta los dados según la opción elegida. */
    private fun updateDiceVisibility() {
        val count = diceCount()
        binding.textDie2.isVisible = count >= 2
        binding.textDie3.isVisible = count >= 3
        binding.textBetTotal.text = getString(R.string.dice_bet_total, stake())
    }

    private fun diceCount(): Int = when (binding.groupDiceCount.checkedRadioButtonId) {
        R.id.radioTwo -> 2
        R.id.radioThree -> 3
        else -> 1
    }

    /** Apuesta de la ronda: diez fichas por cada dado lanzado. */
    private fun stake(): Double = diceCount() * 10.0

    /**
     * Acción del botón Lanzar (declarada con android:onClick en activity_dice.xml).
     */
    fun onRollClicked(view: View) {
        val count = diceCount()
        val values = (1..count).map { Random.nextInt(1, 7) }

        binding.textDie1.text = DICE_FACES[values[0] - 1]
        if (count >= 2) binding.textDie2.text = DICE_FACES[values[1] - 1]
        if (count >= 3) binding.textDie3.text = DICE_FACES[values[2] - 1]

        val total = values.sum()
        val faces = values.joinToString("  ") { DICE_FACES[it - 1] }
        binding.textResult.text = getString(R.string.dice_result_multi, faces, total)

        rolls++
        binding.textRolls.text = getString(R.string.dice_roll_count, rolls)

        // Resolución de la apuesta: se gana cuando el promedio supera 3.5.
        val bet = stake()
        val win = total > count * 3.5
        balance = if (win) balance + bet else balance - bet
        toast(if (win) getString(R.string.dice_win, bet) else getString(R.string.dice_lose, bet))
    }

    private fun chooseSkin() {
        val skinsNames = resources.getStringArray(R.array.skins)
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dice_skins)
            .setSingleChoiceItems(skinsNames, skin) { dialog, which ->
                skin = which
                dialog.dismiss()
                toast(skinsNames[which])
            }
            .show()
    }

    private fun chooseRoom() {
        val rooms = arrayOf("Mesa 1 · $10", "Mesa 2 · $25", "Mesa 3 · $50", "Sala VIP · $100")
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dice_rooms)
            .setItems(rooms) { _, which -> toast(rooms[which]) }
            .show()
    }

    private fun toast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
