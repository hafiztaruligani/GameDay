package com.hafiztaruligani.gamesday.domain.usecase

import androidx.paging.*
import com.hafiztaruligani.gamesday.data.paging.GamesRemoteMediator
import com.hafiztaruligani.gamesday.domain.model.GameSimple
import com.hafiztaruligani.gamesday.domain.repository.GamesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


@OptIn(ExperimentalPagingApi::class)
class GetGamesPaged @Inject constructor(private val gamesRepository: GamesRepository) {

    operator fun invoke(query: String): Flow<PagingData<GameSimple>> {

        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = GamesRemoteMediator(gamesRepository, query),
            pagingSourceFactory = { gamesRepository.getGamesPaged() }
        ).flow.map {

            // transforming from game simple entity into game simple
            it.map { gameSimpleEntity -> gameSimpleEntity.toGameSimple() }

        }
    }

}