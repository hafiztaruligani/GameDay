package com.hafiztaruligani.gamesday.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hafiztaruligani.gamesday.domain.model.GameDetail
import com.hafiztaruligani.gamesday.domain.model.GameSimple

@Entity (tableName = "game_detail")
data class GameDetailEntity (
    @PrimaryKey (autoGenerate = false)
    val id: Int,
    val name: String,
    val releaseDate: String,
    val rating: String,
    val image: String,
    val description: String,
    val company: String,
    val playCount: Int,
    val favorite: Boolean
){
    fun toGameDetail() = GameDetail(
        id = id,
        name = name,
        releaseDate = releaseDate,
        rating = rating,
        image = image,
        description = description,
        company = company,
        playCount = playCount.toString(),
        favorite = favorite
    )

    fun toGameSimple() = GameSimple(
        id = id,
        name = name,
        releaseDate = releaseDate,
        rating = rating,
        image = image
    )

}
