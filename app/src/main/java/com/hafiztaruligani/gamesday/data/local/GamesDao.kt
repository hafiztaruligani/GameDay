package com.hafiztaruligani.gamesday.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.hafiztaruligani.gamesday.data.local.entities.GameDetailEntity
import com.hafiztaruligani.gamesday.data.local.entities.GameSimpleEntity
import com.hafiztaruligani.gamesday.data.local.entities.RemoteKeyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GamesDao {

    // game paging
    @Query ("SELECT * FROM games_simple ORDER BY createdAt ASC")
    fun getGamesPaged(): PagingSource<Int, GameSimpleEntity>

    @Insert (onConflict = REPLACE)
    suspend fun insertGameSimple(gameSimpleEntity: GameSimpleEntity)

    @Query ("DELETE FROM games_simple")
    suspend fun deleteGamesSimple()

    // remote key
    @Insert (onConflict = REPLACE)
    suspend fun insertRemoteKey(remoteKeyEntity: RemoteKeyEntity)

    @Query ("SELECT * FROM remote_keys WHERE id=:id")
    suspend fun getRemoteKey(id: String):RemoteKeyEntity?

    @Query ("DELETE FROM remote_keys WHERE id=:id")
    suspend fun deleteRemoteKey(id: String)

    // game detail
    @Insert (onConflict = REPLACE)
    suspend fun insertGameDetail(gameDetail: GameDetailEntity)

    @Query ("SELECT * FROM game_detail WHERE id=:gameId")
    fun getGameDetail(gameId: Int): Flow<GameDetailEntity>

    // favorite
    @Query ("SELECT * FROM game_detail WHERE favorite=1")
    fun getFavoriteGames(): Flow<List<GameDetailEntity>>

    @Query ("UPDATE game_detail SET favorite=:value WHERE id=:gameId")
    suspend fun updateFavorite(gameId: Int, value: Boolean)

}
