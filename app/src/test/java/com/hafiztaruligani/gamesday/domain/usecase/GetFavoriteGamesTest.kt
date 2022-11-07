package com.hafiztaruligani.gamesday.domain.usecase

import com.hafiztaruligani.cryptoday.util.Resource
import com.hafiztaruligani.gamesday.HttpExceptionBuilder
import com.hafiztaruligani.gamesday.data.repository.FakeGamesRepositoryImpl
import com.hafiztaruligani.gamesday.domain.model.GameSimple
import com.hafiztaruligani.gamesday.domain.repository.GamesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import okio.IOException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@OptIn(ExperimentalCoroutinesApi::class)
@Extensions(
    ExtendWith(MockitoExtension::class)
)
internal class GetFavoriteGamesTest{

    @Mock
    private lateinit var gamesRepository: GamesRepository
    private lateinit var getFavoriteGames: Flow<Resource<List<GameSimple>>>

    @BeforeEach
    fun setup() = runTest{
        getFavoriteGames = GetFavoriteGames(gamesRepository).invoke()
    }

    @Test
    fun success()= runTest{

        val fakeGamesRepository = FakeGamesRepositoryImpl()
        val getFavoriteGames = GetFavoriteGames(fakeGamesRepository).invoke()
        if(getFavoriteGames.last() is Resource.Success) {

            // each returned data is favorite
            (getFavoriteGames.last() as Resource.Success).data.forEach { data ->
                println(data.id)
                assertNotNull(
                    fakeGamesRepository.gamesDetailEntity.find { it.id == data.id && it.favorite }
                )
            }

        }else{
            fail("resource not success")
        }

    }

    @Test
    fun `failed, http exception`()= runTest {
        Mockito.`when`(gamesRepository.getFavoriteGames()).then{ throw HttpExceptionBuilder.NotFoundException().exception }
        assertTrue(
           getFavoriteGames.last() is Resource.Error
        )
    }

    @Test
    fun `failed, network unavailable`()= runTest {
        Mockito.`when`(gamesRepository.getFavoriteGames()).then{ throw IOException() }
        assertTrue(
            getFavoriteGames.last() is Resource.Error
        )
    }


}