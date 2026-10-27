package com.elazar.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elazar.app.databinding.ItemPlayerBinding

/**
 * Adaptador de la lista horizontal de jugadores en línea.
 */
class PlayerAdapter(private val items: List<Player>) :
    RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {

    inner class PlayerViewHolder(val binding: ItemPlayerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(player: Player) {
            binding.textPlayerName.text = player.name
            binding.textPlayerStatus.text = player.status
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding = ItemPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size
}
