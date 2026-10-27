package com.elazar.app

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.elazar.app.databinding.ActivityHistoryBinding
import com.elazar.app.databinding.ItemHistoryBinding

/**
 * Vista adicional. Historial de partidas.
 *
 * Los registros son de ejemplo para el prototipo; en la versión final se
 * leerán desde el backend del jugador.
 */
class HistoryActivity : BaseActivity() {

    private lateinit var binding: ActivityHistoryBinding

    private data class Entry(
        val gameRes: Int,
        val iconRes: Int,
        val dateRes: Int,
        val bet: Double,
        val result: Double
    )

    private val entries = listOf(
        Entry(R.string.history_game_wheel, R.drawable.ic_game_wheel, R.string.history_today, 100.0, 140.0),
        Entry(R.string.history_game_rps, R.drawable.ic_game_rps, R.string.history_today, 40.0, 80.0),
        Entry(R.string.history_game_dice, R.drawable.ic_game_dice, R.string.history_yesterday, 30.0, 120.0),
        Entry(R.string.history_game_snakes, R.drawable.ic_game_snakes, R.string.history_two_days, 25.0, 50.0),
        Entry(R.string.history_game_shooter, R.drawable.ic_game_shooter, R.string.history_two_days, 20.0, -30.0),
        Entry(R.string.history_game_poker, R.drawable.ic_game_poker, R.string.history_three_days, 10.0, -20.0)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(binding.toolbar)
        renderHistory()
    }

    private fun renderHistory() {
        val inflater = LayoutInflater.from(this)
        binding.listHistory.removeAllViews()

        entries.forEach { entry ->
            val row = ItemHistoryBinding.inflate(inflater, binding.listHistory, false)
            row.imageHistoryGame.setImageResource(entry.iconRes)
            row.textHistoryGame.setText(entry.gameRes)
            row.textHistoryDate.text = getString(
                R.string.history_item_result,
                getString(entry.dateRes),
                getString(R.string.history_bet, entry.bet)
            )

            val positive = entry.result >= 0
            row.textHistoryResult.text =
                (if (positive) "+" else "−") + money(kotlin.math.abs(entry.result))
            row.textHistoryResult.setTextColor(
                ContextCompat.getColor(
                    this,
                    if (positive) R.color.azar_green else R.color.azar_red
                )
            )
            binding.listHistory.addView(row.root)
        }
    }
}
