package com.elazar.app

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.elazar.app.databinding.ActivityPokerBinding

/**
 * Vista adicional. Mesa de póker de cinco cartas.
 *
 * La lógica es deliberadamente sencilla: se reparte una mano aleatoria, se
 * evalúa la jugada y se ajusta el saldo según una tabla de pagos.
 */
class PokerActivity : BaseActivity() {

    private lateinit var binding: ActivityPokerBinding
    private val cards: List<TextView> by lazy {
        listOf(binding.card1, binding.card2, binding.card3, binding.card4, binding.card5)
    }

    private data class Card(val rank: Int, val suit: Int)

    private val suitSymbols = arrayOf("♠", "♥", "♦", "♣")
    private val rankLabels = arrayOf("2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A")

    /** Multiplicador de pago (incluye la apuesta). 0 = mano perdedora. */
    private val payouts = intArrayOf(0, 2, 3, 4, 6, 8, 11, 20, 50)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(binding.toolbar)
        updateBalance()
        hideCards()

        binding.buttonDeal.setOnClickListener { deal() }
    }

    private fun bet(): Double = when (binding.groupPokerBet.checkedRadioButtonId) {
        R.id.pokerBet25 -> 25.0
        R.id.pokerBet50 -> 50.0
        R.id.pokerBet100 -> 100.0
        else -> 10.0
    }

    private fun updateBalance() {
        binding.textPokerBalance.text = getString(R.string.balance_short, money(balance))
    }

    private fun hideCards() {
        cards.forEach { card ->
            card.text = "?"
            card.background = ContextCompat.getDrawable(this, R.drawable.bg_card_back)
            card.setTextColor(ContextCompat.getColor(this, R.color.white))
        }
    }

    /** Reparte cinco cartas, evalúa la mano y resuelve la apuesta. */
    private fun deal() {
        val stake = bet()
        if (balance < stake) {
            toast(getString(R.string.poker_no_balance))
            return
        }

        val deck = buildDeck().shuffled()
        val hand = deck.take(5)
        showHand(hand)

        val category = evaluate(hand)
        val handName = getString(handNameRes(category))
        val payout = payouts[category] * stake

        balance -= stake
        if (payout > 0) {
            balance += payout
            val net = payout - stake
            binding.textPokerHand.setTextColor(ContextCompat.getColor(this, R.color.azar_orange))
            binding.textPokerHand.text = handName
            binding.textPokerResult.text = getString(R.string.poker_win, handName, net)
            binding.textPokerResult.setTextColor(ContextCompat.getColor(this, R.color.azar_cream))
        } else {
            binding.textPokerHand.setTextColor(ContextCompat.getColor(this, R.color.azar_orange_light))
            binding.textPokerHand.text = handName
            binding.textPokerResult.text = getString(R.string.poker_lose, handName, stake)
            binding.textPokerResult.setTextColor(ContextCompat.getColor(this, R.color.azar_cream))
        }

        updateBalance()
    }

    private fun showHand(hand: List<Card>) {
        hand.forEachIndexed { index, card ->
            val view = cards[index]
            view.text = "${rankLabels[card.rank - 2]}${suitSymbols[card.suit]}"
            view.background = ContextCompat.getDrawable(this, R.drawable.bg_playing_card)
            val red = card.suit == 1 || card.suit == 2
            view.setTextColor(
                ContextCompat.getColor(this, if (red) R.color.azar_red else R.color.azar_brown)
            )
        }
    }

    private fun buildDeck(): List<Card> =
        (2..14).flatMap { rank -> (0..3).map { suit -> Card(rank, suit) } }

    /** Evalúa la mano y devuelve una categoría de 0 (carta alta) a 8 (escalera de color). */
    private fun evaluate(hand: List<Card>): Int {
        val ranks = hand.map { it.rank }.sorted()
        val suits = hand.map { it.suit }
        val flush = suits.distinct().size == 1

        val distinct = ranks.distinct()
        val straight = distinct.size == 5 && (
            distinct.last() - distinct.first() == 4 ||
                distinct == listOf(2, 3, 4, 5, 14)
            )

        val counts = ranks.groupingBy { it }.eachCount().values.sortedDescending()

        return when {
            flush && straight -> 8
            counts.first() == 4 -> 7
            counts == listOf(3, 2) -> 6
            flush -> 5
            straight -> 4
            counts.first() == 3 -> 3
            counts.take(2) == listOf(2, 2) -> 2
            counts.first() == 2 -> 1
            else -> 0
        }
    }

    private fun handNameRes(category: Int): Int = when (category) {
        8 -> R.string.poker_hand_straight_flush
        7 -> R.string.poker_hand_four
        6 -> R.string.poker_hand_full_house
        5 -> R.string.poker_hand_flush
        4 -> R.string.poker_hand_straight
        3 -> R.string.poker_hand_trips
        2 -> R.string.poker_hand_two_pair
        1 -> R.string.poker_hand_pair
        else -> R.string.poker_hand_high
    }

    private fun toast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
