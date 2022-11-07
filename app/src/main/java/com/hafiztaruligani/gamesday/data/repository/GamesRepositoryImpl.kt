package com.hafiztaruligani.gamesday.data.repository

import androidx.paging.PagingSource
import com.hafiztaruligani.gamesday.data.local.GamesDao
import com.hafiztaruligani.gamesday.data.local.entities.GameDetailEntity
import com.hafiztaruligani.gamesday.data.local.entities.GameSimpleEntity
import com.hafiztaruligani.gamesday.data.local.entities.RemoteKeyEntity
import com.hafiztaruligani.gamesday.data.remote.ApiService
import com.hafiztaruligani.gamesday.data.remote.dto.gameslist.GamesListResponse
import com.hafiztaruligani.gamesday.domain.repository.GamesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class GamesRepositoryImpl(
    private val apiService: ApiService,
    private val gamesDao: GamesDao
) : GamesRepository {

    // Game paging
    override suspend fun insertGameSimple(gameSimpleEntity: GameSimpleEntity) {
        gamesDao.insertGameSimple(gameSimpleEntity)
    }

    override fun getGamesPaged(): PagingSource<Int, GameSimpleEntity> {
        return gamesDao.getGamesPaged()
    }

    override suspend fun deleteGamesSimple() {
        gamesDao.deleteGamesSimple()
    }

    // Remote Key
    override suspend fun insertRemoteKey(remoteKeyEntity: RemoteKeyEntity) {
        gamesDao.insertRemoteKey(remoteKeyEntity)
    }

    override suspend fun getRemoteKey(id: String): RemoteKeyEntity? {
        return gamesDao.getRemoteKey(id)
    }

    override suspend fun deleteRemoteKey(id: String) {
        gamesDao.deleteRemoteKey(id)
    }

    // Game detail
    override suspend fun getGameDetail(gameId: Int):Flow<GameDetailEntity?> {

        // check is favorite game
        val favorite = gamesDao.getGameDetail(gameId).firstOrNull()?.favorite ?: false

        // get from remote and map into game detail entity (with isFavoriteGame parameter)
        val data = apiService.getGameDetail(gameId).toGameDetailEntity(favorite)

        gamesDao.insertGameDetail(data)

        return getGameDetailLocalCache(gameId)
    }

    override fun getGameDetailLocalCache(gameId: Int): Flow<GameDetailEntity?> {
        return gamesDao.getGameDetail(gameId)
    }

    // Favorite
    override suspend fun addFavorite(gameId: Int) {
        gamesDao.updateFavorite(gameId, true)
    }

    override suspend fun deleteFavorite(gameId: Int) {
        gamesDao.updateFavorite(gameId, false)
    }

    override fun getFavoriteGames(): Flow<List<GameDetailEntity>> {
        return gamesDao.getFavoriteGames()
    }

    // Remote
    override suspend fun getGamesRemote(
        query: String,
        page: Int,
        pageSize: Int
    ): GamesListResponse {
        return apiService.getGames(
            query = query,
            page = page,
            pageSize = pageSize
        )
    }

}
