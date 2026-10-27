package com.elazar.app

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Modelo de un juego de azar mostrado en el tablero principal.
 */
data class Game(
    val id: String,
    @StringRes val nameRes: Int,
    @DrawableRes val iconRes: Int,
    val online: Int,
    @StringRes val badgeRes: Int? = null,
    val popular: Boolean = false,
    val live: Boolean = false
)

/**
 * Catálogo de juegos de El Azar (los seis juegos dibujados en el prototipado en papel).
 */
object Games {

    val ALL: List<Game> = listOf(
        Game("dice", R.string.game_dice, R.drawable.ic_game_dice, 412, R.string.dice_1v1, popular = true, live = true),
        Game("snakes", R.string.game_snakes, R.drawable.ic_game_snakes, 286, live = true),
        Game("rps", R.string.game_rps, R.drawable.ic_game_rps, 531, R.string.rps_all_or_nothing, popular = true),
        Game("poker", R.string.game_poker, R.drawable.ic_game_poker, 148),
        Game("shooter", R.string.game_shooter, R.drawable.ic_game_shooter, 197, popular = true),
        Game("wheel", R.string.game_wheel, R.drawable.ic_game_wheel, 352, R.string.game_wheel_turns, live = true)
    )
}

/**
 * Articulo de la tienda de recompensas.
 */
data class Article(
    val index: Int,
    val price: Double,
    @DrawableRes val iconRes: Int
)

/**
 * Jugador conectado que aparece en las salas.
 */
data class Player(val name: String, val status: String)
