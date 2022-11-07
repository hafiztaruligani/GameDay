package com.hafiztaruligani.gamesday.data.paging

import androidx.paging.*
import com.hafiztaruligani.gamesday.HttpExceptionBuilder
import com.hafiztaruligani.gamesday.data.local.entities.GameSimpleEntity
import com.hafiztaruligani.gamesday.data.local.entities.RemoteKeyEntity
import com.hafiztaruligani.gamesday.data.remote.dto.gameslist.GameItemResponse
import com.hafiztaruligani.gamesday.data.remote.dto.gameslist.GamesListResponse
import com.hafiztaruligani.gamesday.domain.repository.GamesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@OptIn(
    ExperimentalCoroutinesApi::class,
    ExperimentalPagingApi::class
)
@Extensions(value = [
    ExtendWith(MockitoExtension::class)
])
internal class GamesRemoteMediatorTest{

    @Mock
    lateinit var gamesRepository: GamesRepository
    private val pageSize = 20
    private val pagingState = PagingState<Int, GameSimpleEntity>(
        listOf(),
        null,
        PagingConfig(pageSize),
        10
    )

    @Test
    fun ` REFRESH success, endOffPagination = false`() = runTest{
        // mock remote data
        Mockito.`when`(gamesRepository.getGamesRemote("", 1, pageSize)).then{
            GamesListResponse( next = "asd", results = listOf( GameItemResponse(id=1) ) )
        }

        val result = GamesRemoteMediator(gamesRepository, "").load(LoadType.REFRESH, pagingState)

        assertFalse( (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached )

    }

    @Test
    fun `APPEND success, endOffPagination = FALSE`() = runTest{
        val page = 2

        // mock remote data
        Mockito.`when`(gamesRepository.getGamesRemote("", page, pageSize)).then{
            GamesListResponse( next = "asd", results = listOf( GameItemResponse(id=1) ) )
        }

        // mock remote key
        Mockito.`when`(gamesRepository.getRemoteKey(
            GamesRemoteMediator.GAMES_REMOTE_KEYS
        )).then {
            RemoteKeyEntity(
                GamesRemoteMediator.GAMES_REMOTE_KEYS,
                page
            )
        }

        val result = GamesRemoteMediator(gamesRepository, "").load(LoadType.APPEND, pagingState)

        assertFalse( (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached )

    }

    @Test
    fun `APPEND success, endOffPagination = TRUE`() = runTest{
        val page = 2

        // when Response "next" null
        // mock remote data
        Mockito.`when`(gamesRepository.getGamesRemote("", page, pageSize)).then{
            GamesListResponse( next = null, results = listOf( GameItemResponse(id=1) ) )
        }

        // mock remote key
        Mockito.`when`(gamesRepository.getRemoteKey(
            GamesRemoteMediator.GAMES_REMOTE_KEYS
        )).then {
            RemoteKeyEntity(
                GamesRemoteMediator.GAMES_REMOTE_KEYS,
                page
            )
        }

        var result = GamesRemoteMediator(gamesRepository, "").load(LoadType.APPEND, pagingState)
        assertTrue( (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached )

        // when Response result empty
        // mock remote data
        Mockito.`when`(gamesRepository.getGamesRemote("", page, pageSize)).then{
            GamesListResponse( next = "asd", results = listOf() )
        }

        result = GamesRemoteMediator(gamesRepository, "").load(LoadType.APPEND, pagingState)
        assertTrue( (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached )
    }

    @Test
    fun `REFRESH and APPEND error`()= runTest{
        // mock remote data
        Mockito.`when`(gamesRepository.getGamesRemote("", 1, pageSize)).then{
            throw HttpExceptionBuilder.NotFoundException().exception
        }
        // mock remote key
        Mockito.`when`(gamesRepository.getRemoteKey(
            GamesRemoteMediator.GAMES_REMOTE_KEYS
        )).then {
            RemoteKeyEntity(
                GamesRemoteMediator.GAMES_REMOTE_KEYS,
                1
            )
        }

        // REFRESH
        var result = GamesRemoteMediator(gamesRepository, "").load(LoadType.REFRESH, pagingState)
        assertTrue( result is RemoteMediator.MediatorResult.Error)

        // APPEND
        result = GamesRemoteMediator(gamesRepository, "").load(LoadType.APPEND, pagingState)
        assertTrue( result is RemoteMediator.MediatorResult.Error)

    }

}