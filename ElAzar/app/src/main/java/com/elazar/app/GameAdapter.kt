package com.elazar.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elazar.app.databinding.ItemGameBinding

/**
 * Adaptador de la rejilla de juegos del dashboard (2 columnas).
 */
class GameAdapter(
    private var items: List<Game>,
    private val onClick: (Game) -> Unit
) : RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    inner class GameViewHolder(val binding: ItemGameBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(game: Game) {
            val context = binding.root.context
            binding.imageGame.setImageResource(game.iconRes)
            binding.textGameName.setText(game.nameRes)
            binding.textGamePlayers.text = context.getString(R.string.players_online, "${game.online}")
            if (game.badgeRes != null) {
                binding.textGameBadge.visibility = android.view.View.VISIBLE
                binding.textGameBadge.setText(game.badgeRes)
            } else {
                binding.textGameBadge.visibility = android.view.View.GONE
            }
            binding.root.setOnClickListener { onClick(game) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding = ItemGameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    fun submit(list: List<Game>) {
        items = list
        notifyDataSetChanged()
    }
}
