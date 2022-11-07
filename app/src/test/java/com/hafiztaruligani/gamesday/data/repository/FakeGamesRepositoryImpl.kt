package com.hafiztaruligani.gamesday.data.repository

import androidx.paging.PagingSource
import com.hafiztaruligani.gamesday.data.local.entities.GameDetailEntity
import com.hafiztaruligani.gamesday.data.local.entities.GameSimpleEntity
import com.hafiztaruligani.gamesday.data.local.entities.RemoteKeyEntity
import com.hafiztaruligani.gamesday.data.remote.dto.gameslist.GamesListResponse
import com.hafiztaruligani.gamesday.domain.repository.GamesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

internal class FakeGamesRepositoryImpl: GamesRepository{

     var gamesDetailEntity = mutableListOf<GameDetailEntity>()

    init {
        repeat(10){
            gamesDetailEntity.add(
                GameDetailEntity(
                    id = it,
                    name = "name",
                    releaseDate = "releaseDate",
                    rating = "rating",
                    image = "image",
                    description = "description",
                    company = "company",
                    playCount = 11,
                    favorite = it % 2 == 0
                )
            )
        }
    }

    override suspend fun insertGameSimple(gameSimpleEntity: GameSimpleEntity) {
        throw IOException("No Implementation")
    }

    override fun getGamesPaged(): PagingSource<Int, GameSimpleEntity> {
        throw IOException("No Implementation")
    }

    override suspend fun deleteGamesSimple() {
        throw IOException("No Implementation")
    }

    override suspend fun insertRemoteKey(remoteKeyEntity: RemoteKeyEntity) {
        throw IOException("No Implementation")
    }

    override suspend fun getRemoteKey(id: String): RemoteKeyEntity? {
        throw IOException("No Implementation")
    }

    override suspend fun deleteRemoteKey(id: String) {
        throw IOException("No Implementation")
    }

    override suspend fun getGamesRemote(
        query: String,
        page: Int,
        pageSize: Int
    ): GamesListResponse {
        throw IOException("No Implementation")
    }

    override suspend fun getGameDetail(gameId: Int): Flow<GameDetailEntity> {
        throw IOException("No Implementation")
    }

    override fun getGameDetailLocalCache(gameId: Int): Flow<GameDetailEntity> {
        throw IOException("No Implementation")
    }

    override fun getFavoriteGames(): Flow<List<GameDetailEntity>> {
        return flow {
            emit(
                gamesDetailEntity.filter { it.favorite }
            )
        }
    }

    override suspend fun addFavorite(gameId: Int) {
        changeFavoriteValue(gameId, true)
    }

    override suspend fun deleteFavorite(gameId: Int) {
        changeFavoriteValue(gameId, false)
    }

    private fun changeFavoriteValue(gameId: Int, value: Boolean){
        val index = gamesDetailEntity.indexOfFirst { it.id == gameId }
        gamesDetailEntity[index] = gamesDetailEntity[index].copy(favorite = value)
    }


}