package com.hafiztaruligani.gamesday.domain.usecase

import com.hafiztaruligani.gamesday.data.repository.FakeGamesRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class SetFavoriteTest{

    private lateinit var gamesRepository: FakeGamesRepositoryImpl
    private lateinit var setFavorite: SetFavorite
    @BeforeEach
    fun setup(){
        gamesRepository = FakeGamesRepositoryImpl()
        setFavorite = SetFavorite(gamesRepository)
    }

    @Test
    fun `set to favorite`()= runTest{

        // get not favorite item
        val data = gamesRepository.gamesDetailEntity.firstOrNull { !it.favorite }
        data?.let {

            setFavorite.invoke(data.id, true)

            assertTrue(
              gamesRepository.gamesDetailEntity.first{ it.id == data.id }.favorite
            )

        }?: fail("all data in fake repository is favorite")

    }

    @Test
    fun `set to not favorite`()= runTest{

        // get not favorite item
        val data = gamesRepository.gamesDetailEntity.firstOrNull { it.favorite }
        data?.let {

            setFavorite.invoke(data.id, false)

            assertFalse(
                gamesRepository.gamesDetailEntity.first{ it.id == data.id }.favorite
            )

        }?: fail("all data in fake repository is favorite")

    }

}