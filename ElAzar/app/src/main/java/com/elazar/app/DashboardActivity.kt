package com.elazar.app

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.elazar.app.databinding.ActivityDashboardBinding
import com.elazar.app.databinding.NavHeaderBinding

/**
 * Vista 3. Página principal (dashboard).
 *
 * Muestra el saludo al jugador, su saldo, la rejilla con los seis juegos de azar
 * y los jugadores conectados. Incluye el menú lateral (NavigationView) y el menú
 * de opciones (OptionsMenu) de la Práctica 8.
 */
class DashboardActivity : BaseActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(binding.toolbar, showUp = false)
        setupDrawer()
        setupHeader()
        setupGames()
        setupPlayers()
        setupFilters()
        setupBackNavigation()

        // La lista de juegos no debe robar el foco inicial ni ocultar la cabecera.
        binding.scrollDashboard.requestFocus()
        binding.scrollDashboard.post { binding.scrollDashboard.scrollTo(0, 0) }
    }

    /** El botón "atrás" cierra el menú lateral antes de salir de la pantalla. */
    private fun setupBackNavigation() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        // El saldo y el perfil pueden cambiar en otras pantallas.
        binding.textBalance.text = money(balance)
        binding.textGreeting.text = getString(R.string.greeting, "@$user")
        val header = NavHeaderBinding.bind(binding.navigationView.getHeaderView(0))
        header.textNavBalance.text = money(balance)
        header.textNavUser.text = "@$user"
    }

    /** Configura el cajón de navegación (menú lateral). */
    private fun setupDrawer() {
        drawerToggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.menu_open, R.string.dialog_close
        )
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        binding.navigationView.getHeaderView(0).setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            open(ProfileActivity::class.java)
        }

        binding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dice -> open(DiceGameActivity::class.java)
                R.id.nav_snakes -> open(SnakesActivity::class.java)
                R.id.nav_rps -> open(RpsActivity::class.java)
                R.id.nav_wallet -> open(PaymentActivity::class.java)
                R.id.nav_store -> open(StoreActivity::class.java)
                R.id.nav_privacy -> showDialog(R.string.dialog_privacy_title, R.string.dialog_privacy_body)
                R.id.nav_responsible -> showDialog(R.string.dialog_responsible_title, R.string.dialog_responsible_body)
                R.id.nav_help -> open(HelpActivity::class.java)
                R.id.nav_profile -> open(ProfileActivity::class.java)
                R.id.nav_history -> open(HistoryActivity::class.java)
                R.id.nav_dashboard -> Unit
                R.id.nav_logout -> logout()
                else -> Unit
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    /** Cabecera del menú lateral y saludo del tablero. */
    private fun setupHeader() {
        val header = NavHeaderBinding.bind(binding.navigationView.getHeaderView(0))
        header.textNavUser.text = "@$user"
        header.textNavBalance.text = money(balance)

        binding.textGreeting.text = getString(R.string.greeting, "@$user")
        binding.textOnline.text = getString(R.string.dashboard_online, 1284)

        // Acceso directo al perfil tocando el avatar o el saludo.
        binding.imageAvatar.setOnClickListener { open(ProfileActivity::class.java) }
        binding.textGreeting.setOnClickListener { open(ProfileActivity::class.java) }
    }

    /** Rejilla de juegos (GridLayoutManager de dos columnas). */
    private fun setupGames() {
        binding.recyclerGames.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerGames.adapter = GameAdapter(Games.ALL) { game -> startGame(game) }
    }

    private fun setupPlayers() {
        val players = listOf(
            Player("@usuario.G", getString(R.string.shooter_ready)),
            Player("@maria_7", getString(R.string.shooter_ready)),
            Player("@dado_loco", getString(R.string.shooter_ready)),
            Player("@ruleta22", getString(R.string.shooter_ready)),
            Player("@pokerface", getString(R.string.shooter_ready))
        )
        binding.recyclerPlayers.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerPlayers.adapter = PlayerAdapter(players)
    }

    /** Filtros por categoría del tablero (ChipGroup). */
    private fun setupFilters() {
        val adapter = binding.recyclerGames.adapter as GameAdapter
        binding.chipGroupFilter.setOnCheckedStateChangeListener { _, checkedIds ->
            when (checkedIds.firstOrNull()) {
                R.id.chipPopular -> adapter.submit(Games.ALL.filter { it.popular })
                R.id.chipLive -> adapter.submit(Games.ALL.filter { it.live })
                else -> adapter.submit(Games.ALL)
            }
        }
    }

    /** Abre la mesa del juego seleccionado. */
    private fun startGame(game: Game) {
        when (game.id) {
            "dice" -> open(DiceGameActivity::class.java)
            "snakes" -> open(SnakesActivity::class.java)
            "rps" -> open(RpsActivity::class.java)
            "shooter" -> open(ShooterActivity::class.java)
            "poker" -> open(PokerActivity::class.java)
            "wheel" -> open(RouletteActivity::class.java)
            else -> showDialog(game.nameRes, R.string.dialog_info_body)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)) return true
        if (item.itemId == android.R.id.home) {
            binding.drawerLayout.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
