package com.elazar.app

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.elazar.app.databinding.ActivitySnakesBinding
import kotlin.random.Random

/**
 * Vista 8. Juego "Serpientes y Escaleras".
 *
 * El tablero de 36 casillas se construye en tiempo de ejecución sobre un GridLayout
 * (dentro de un FrameLayout) aplicando el estilo de la marca a cada casilla, lo que
 * demuestra el uso de layouts anidados de la Práctica 8.
 */
class SnakesActivity : BaseActivity() {

    /** Serpientes: casilla de inicio -> casilla de destino. */
    private val snakes = mapOf(17 to 6, 24 to 11, 33 to 21, 36 to 28)

    /** Escaleras: casilla de inicio -> casilla de destino. */
    private val ladders = mapOf(3 to 12, 8 to 15, 19 to 27, 30 to 34)

    private lateinit var binding: ActivitySnakesBinding
    private val cellViews = HashMap<Int, TextView>()
    private var position = 1
    private var round = 1
    private var expanded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySnakesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(binding.toolbar)
        buildBoard()
        setupPlayers()

        binding.textRound.text = getString(R.string.snakes_round, round)
        binding.textPosition.text = getString(R.string.snakes_position, position)
        binding.buttonExpand.setOnClickListener { toggleBoard() }
        paintBoard()
    }

    // ===================================================== tablero
    /** Crea las 36 casillas con GridLayout.spec y pesos iguales. */
    private fun buildBoard() {
        binding.gridBoard.removeAllViews()
        cellViews.clear()
        val margin = (resources.displayMetrics.density * 3).toInt()

        for (gridRow in 0 until 6) {
            for (col in 0 until 6) {
                val number = numberAt(gridRow, col)
                val cell = TextView(this).apply {
                    text = number.toString()
                    gravity = Gravity.CENTER
                    textSize = 11f
                    setTextColor(Color.parseColor("#4A2A15"))
                    setBackgroundResource(R.drawable.bg_board_cell)
                    layoutParams = GridLayout.LayoutParams(
                        GridLayout.spec(col, 1f),
                        GridLayout.spec(gridRow, 1f)
                    ).apply {
                        width = 0
                        height = 0
                        setMargins(margin, margin, margin, margin)
                    }
                }
                cellViews[number] = cell
                binding.gridBoard.addView(cell)
            }
        }
    }

    /** Numeración serpenteante desde la esquina inferior izquierda. */
    private fun numberAt(gridRow: Int, col: Int): Int {
        val rowFromBottom = 5 - gridRow
        val realCol = if (rowFromBottom % 2 == 1) 5 - col else col
        return rowFromBottom * 6 + realCol + 1
    }

    /** Colorea las casillas especiales y la posición del jugador. */
    private fun paintBoard() {
        cellViews.forEach { (number, cell) ->
            val background = GradientDrawable().apply {
                cornerRadius = resources.displayMetrics.density * 8
            }
            when {
                number == position -> {
                    background.setColor(Color.parseColor("#FF7F2A"))
                    cell.setTextColor(Color.WHITE)
                    cell.text = "★ $number"
                }
                snakes.containsKey(number) -> {
                    background.setColor(Color.parseColor("#DCE9DC"))
                    cell.setTextColor(Color.parseColor("#2E7D32"))
                    cell.text = "🐍 $number"
                }
                ladders.containsKey(number) -> {
                    background.setColor(Color.parseColor("#FFE6D5"))
                    cell.setTextColor(Color.parseColor("#AA4400"))
                    cell.text = "🪜 $number"
                }
                else -> {
                    background.setColor(Color.parseColor("#FFE6D5"))
                    cell.setTextColor(Color.parseColor("#4A2A15"))
                    cell.text = number.toString()
                }
            }
            background.setStroke((resources.displayMetrics.density * 1).toInt(), Color.parseColor("#FFB380"))
            cell.background = background
        }
        binding.textPosition.text = getString(R.string.snakes_position, position)
    }

    // ===================================================== listas
    private fun setupPlayers() {
        val players = listOf(
            Player("@${user}", getString(R.string.shooter_ready)),
            Player("@u2", getString(R.string.shooter_ready)),
            Player("@u3", getString(R.string.shooter_ready))
        )
        binding.recyclerPlayers.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerPlayers.adapter = PlayerAdapter(players)
    }

    // ===================================================== juego
    /**
     * Acción del botón Girar (android:onClick en activity_snakes.xml).
     */
    fun onRollClicked(view: View) {
        val dice = Random.nextInt(1, 7)
        binding.textDie.text = DICE_FACES[dice - 1]

        val target = (position + dice).coerceAtMost(36)
        var message = getString(R.string.snakes_move, dice)

        val finalPosition = when {
            snakes.containsKey(target) -> {
                message = getString(R.string.snakes_snake, snakes.getValue(target))
                snakes.getValue(target)
            }
            ladders.containsKey(target) -> {
                message = getString(R.string.snakes_ladder, ladders.getValue(target))
                ladders.getValue(target)
            }
            else -> target
        }

        position = finalPosition
        round++
        binding.textRound.text = getString(R.string.snakes_round, round)
        binding.textMove.text = "$message  ·  ${getString(R.string.snakes_position, position)}"
        paintBoard()

        if (position >= 36) {
            toast(getString(R.string.snakes_winner))
            position = 1
            paintBoard()
        }
    }

    /** "Expandir mapa": alterna la proporción del tablero. */
    private fun toggleBoard() {
        expanded = !expanded
        val params = binding.frameBoard.layoutParams as ConstraintLayout.LayoutParams
        params.dimensionRatio = if (expanded) "3:4" else "1:1"
        binding.frameBoard.layoutParams = params
        binding.buttonExpand.setText(if (expanded) R.string.snakes_collapse else R.string.snakes_expand)
        binding.textPlayersTitle.isVisible = !expanded
        binding.recyclerPlayers.isVisible = !expanded
    }

    private fun toast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
