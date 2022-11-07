package com.hafiztaruligani.gamesday.domain.repository

import androidx.paging.PagingSource
import com.hafiztaruligani.gamesday.data.local.entities.GameDetailEntity
import com.hafiztaruligani.gamesday.data.local.entities.GameSimpleEntity
import com.hafiztaruligani.gamesday.data.local.entities.RemoteKeyEntity
import com.hafiztaruligani.gamesday.data.remote.dto.gameslist.GamesListResponse
import kotlinx.coroutines.flow.Flow

interface GamesRepository {

    // games paging
    suspend fun insertGameSimple(gameSimpleEntity: GameSimpleEntity)
    fun getGamesPaged(): PagingSource<Int, GameSimpleEntity>
    suspend fun deleteGamesSimple()

    // remote keys
    suspend fun insertRemoteKey(remoteKeyEntity: RemoteKeyEntity)
    suspend fun getRemoteKey(id: String): RemoteKeyEntity?
    suspend fun deleteRemoteKey(id: String)

    // remote
    suspend fun getGamesRemote(query: String, page: Int, pageSize: Int): GamesListResponse

    // game detail
    suspend fun getGameDetail(gameId: Int): Flow<GameDetailEntity?>
    fun getGameDetailLocalCache(gameId: Int): Flow<GameDetailEntity?>

    // favorite
    suspend fun addFavorite(gameId: Int)
    suspend fun deleteFavorite(gameId: Int)
    fun getFavoriteGames(): Flow<List<GameDetailEntity>>

}
