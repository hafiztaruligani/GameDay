package com.hafiztaruligani.gamesday.util

import androidx.recyclerview.widget.DiffUtil
import com.hafiztaruligani.gamesday.domain.model.GameSimple

class GamesDiffUtil: DiffUtil.ItemCallback<GameSimple>() {
    override fun areItemsTheSame(oldItem: GameSimple, newItem: GameSimple): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GameSimple, newItem: GameSimple): Boolean {
        return oldItem.name == newItem.name
    }

}
