package com.hafiztaruligani.gamesday.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hafiztaruligani.gamesday.data.local.entities.GameDetailEntity
import com.hafiztaruligani.gamesday.data.local.entities.GameSimpleEntity
import com.hafiztaruligani.gamesday.data.local.entities.RemoteKeyEntity

@Database(
    entities = [
        GameSimpleEntity::class,
        GameDetailEntity::class,
        RemoteKeyEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getGamesDao(): GamesDao

    companion object{
        fun getInstance(context: Context) = Room.databaseBuilder(context, AppDatabase::class.java, "games_day").build()
    }

}