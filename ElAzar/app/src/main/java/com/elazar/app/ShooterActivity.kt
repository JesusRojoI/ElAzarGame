package com.elazar.app

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.elazar.app.databinding.ActivityShooterBinding
import kotlin.math.hypot
import kotlin.random.Random

/**
 * Vista 10. Juego "Disparos".
 *
 * El jugador apunta tocando el blanco y dispara; la distancia al centro determina
 * los puntos obtenidos. Se practican ImageView, Button, ChipGroup y TextView de
 * resultados sobre un ConstraintLayout.
 */
class ShooterActivity : BaseActivity() {

    private lateinit var binding: ActivityShooterBinding
    private var score = 0
    private var ammo = MAX_AMMO
    private var aimX = 0f
    private var aimY = 0f

    private companion object {
        const val MAX_AMMO = 6
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShooterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(binding.toolbar)
        updateHud()

        binding.frameTarget.setOnClickListener { onTargetTouched() }
        binding.chipGroupSkins.setOnCheckedStateChangeListener { _, ids ->
            if (ids.isNotEmpty()) toast(getString(R.string.shooter_skin_choose))
        }
        binding.buttonDuel.setOnClickListener { toast(getString(R.string.shooter_gun_1v1)) }
        binding.buttonVsPc.setOnClickListener { toast(getString(R.string.shooter_gun_pc)) }
        binding.buttonSkin.setOnClickListener { toast(getString(R.string.shooter_skin)) }
        binding.buttonLobby.setOnClickListener { toast(getString(R.string.shooter_lobby_label)) }
    }

    /** Coloca la mira en un punto aleatorio del blanco al tocarlo. */
    @SuppressLint("ClickableViewAccessibility")
    private fun onTargetTouched() {
        val max = binding.frameTarget.width / 2f
        aimX = Random.nextFloat() * max * 1.4f - max * 0.7f
        aimY = Random.nextFloat() * max * 1.4f - max * 0.7f
        binding.textImpact.translationX = aimX
        binding.textImpact.translationY = aimY
        binding.textImpact.isVisible = true
    }

    /**
     * Acción del botón Disparar (android:onClick en activity_shooter.xml).
     */
    fun onFireClicked(view: View) {
        if (ammo <= 0) {
            ammo = MAX_AMMO
            toast(getString(R.string.shooter_reload))
            updateHud()
            return
        }

        ammo--
        val distance = hypot(aimX, aimY)
        val radius = binding.frameTarget.width / 2f

        when {
            distance <= radius * 0.25f -> {
                score += 20
                toast(getString(R.string.shooter_hit, 20))
            }
            distance <= radius * 0.55f -> {
                score += 10
                toast(getString(R.string.shooter_hit, 10))
            }
            else -> toast(getString(R.string.shooter_miss))
        }
        updateHud()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("score", score)
        outState.putInt("ammo", ammo)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        score = savedInstanceState.getInt("score", 0)
        ammo = savedInstanceState.getInt("ammo", MAX_AMMO)
        updateHud()
    }

    private fun updateHud() {
        binding.textScore.text = getString(R.string.shooter_score, score)
        binding.textAmmo.text = getString(R.string.shooter_ammo_full, ammo, MAX_AMMO)
    }

    private fun toast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
