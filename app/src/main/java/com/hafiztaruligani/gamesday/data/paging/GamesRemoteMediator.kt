package com.hafiztaruligani.gamesday.data.paging

import androidx.paging.*
import com.hafiztaruligani.gamesday.data.local.entities.GameSimpleEntity
import com.hafiztaruligani.gamesday.data.local.entities.RemoteKeyEntity
import com.hafiztaruligani.gamesday.domain.repository.GamesRepository
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class GamesRemoteMediator(
    private val gamesRepository: GamesRepository,
    private val query: String
) : RemoteMediator<Int, GameSimpleEntity>() {

    companion object{
        const val GAMES_REMOTE_KEYS = "games_simple_remote_key"
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, GameSimpleEntity>
    ): MediatorResult {

        try {

            // initiate current page
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    gamesRepository.getRemoteKey(GAMES_REMOTE_KEYS)?.nextKey ?:
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            // get data from remote
            val response = gamesRepository.getGamesRemote(query, page, state.config.pageSize)
            val data = response.results?.filterNotNull() ?: listOf()

            // delete old data if load type is REFRESH
            if(loadType == LoadType.REFRESH) {

                gamesRepository.deleteGamesSimple()
                gamesRepository.deleteRemoteKey(GAMES_REMOTE_KEYS)
            }

            // insert new data
            data.forEach {
                gamesRepository.insertGameSimple(it.toGameSimpleEntity())
            }
            gamesRepository.insertRemoteKey( RemoteKeyEntity(id = GAMES_REMOTE_KEYS, page+1) )

            return MediatorResult.Success( endOfPaginationReached = response.next == null || data.isEmpty() )

        } catch (e: HttpException){
            return MediatorResult.Error(e)
        } catch (e: IOException){
            return MediatorResult.Error(e)
        }

    }

}