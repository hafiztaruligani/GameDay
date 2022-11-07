package com.hafiztaruligani.gamesday.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hafiztaruligani.gamesday.domain.model.GameSimple

@Entity (tableName = "games_simple")
data class GameSimpleEntity(
    @PrimaryKey (autoGenerate = false)
    val id: Int,
    val name: String,
    val releaseDate: String,
    val rating: Double,
    val image: String,
    val createdAt: Long
){
    fun toGameSimple() = GameSimple(
        id = id,
        name = name,
        releaseDate = releaseDate,
        rating = if (rating>0.0) rating.toString() else "-",
        image = image
    )
}
