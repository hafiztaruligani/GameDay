package com.hafiztaruligani.gamesday.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameSimple (
    val id: Int,
    val name: String,
    val releaseDate: String,
    val rating: String,
    val image: String
) : Parcelable
