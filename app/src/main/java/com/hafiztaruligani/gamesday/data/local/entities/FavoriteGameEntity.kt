package com.hafiztaruligani.gamesday.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "favorite_games")
data class FavoriteGameEntity(
    @PrimaryKey (autoGenerate = false)
    val id: Int
)
