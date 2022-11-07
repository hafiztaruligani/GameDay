package com.hafiztaruligani.gamesday.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey (autoGenerate = false)
    val id: String,
    val nextKey : Int?
)
