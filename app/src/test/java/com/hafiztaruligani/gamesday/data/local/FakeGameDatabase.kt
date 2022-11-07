package com.hafiztaruligani.gamesday.data.local

import androidx.paging.PagingSource
import com.hafiztaruligani.gamesday.data.local.entities.GameDetailEntity
import com.hafiztaruligani.gamesday.data.local.entities.GameSimpleEntity
import com.hafiztaruligani.gamesday.data.local.entities.RemoteKeyEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class FakeGameDatabase : GamesDao {

    var gamesDetailEntity = mutableListOf<GameDetailEntity>()
    var gamesSimpleEntity = mutableListOf<GameSimpleEntity>()
    var remoteKeyEntity = mutableListOf<RemoteKeyEntity>()

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
            gamesSimpleEntity.add(
                GameSimpleEntity(
                    id = it,
                    name = "name",
                    releaseDate = "releaseDate",
                    rating = 1.1,
                    image = "image",
                    createdAt = 1231+it.toLong()
                )
            )
            remoteKeyEntity.add(
                RemoteKeyEntity(it.toString(), it)
            )
        }
    }

    override fun getGamesPaged(): PagingSource<Int, GameSimpleEntity> {
        throw IOException("No Implementation")
    }

    override suspend fun insertGameSimple(gameSimpleEntity: GameSimpleEntity) {
        gamesSimpleEntity.add(gameSimpleEntity)
    }

    override suspend fun deleteGamesSimple() {
        gamesSimpleEntity = mutableListOf()
    }

    override suspend fun insertRemoteKey(remoteKeyEntity: RemoteKeyEntity) {
        this.remoteKeyEntity.add(remoteKeyEntity)
    }

    override suspend fun getRemoteKey(id: String): RemoteKeyEntity? {
        return remoteKeyEntity.firstOrNull{ it.id == id }
    }

    override suspend fun deleteRemoteKey(id: String) {
        remoteKeyEntity.removeIf { it.id == id }
    }

    override suspend fun insertGameDetail(gameDetail: GameDetailEntity) {
        gamesDetailEntity.add(gameDetail)
    }

    override fun getFavoriteGames(): Flow<List<GameDetailEntity>> = flow {
        emit(gamesDetailEntity.filter { it.favorite })
    }

    override fun getGameDetail(gameId: Int): Flow<GameDetailEntity> = flow {
        gamesDetailEntity.firstOrNull{ it.id == gameId }?.let {
            emit(it)
        }
    }

    override suspend fun updateFavorite(gameId: Int, value: Boolean) {
        val index = gamesDetailEntity.indexOfFirst { it.id == gameId }
        gamesDetailEntity[index] = gamesDetailEntity[index].copy(favorite = value)
    }

    fun getValidRemoteKey() = remoteKeyEntity.first()
    fun getValidGameDetail(favorite: Boolean?=null) =
        favorite?.let { gamesDetailEntity.filter { it.favorite == favorite } }?.firstOrNull()?:
        gamesDetailEntity.first()

}