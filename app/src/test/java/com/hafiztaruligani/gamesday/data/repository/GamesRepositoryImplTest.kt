package com.hafiztaruligani.gamesday.data.repository

import com.hafiztaruligani.gamesday.HttpExceptionBuilder
import com.hafiztaruligani.gamesday.data.local.FakeGameDatabase
import com.hafiztaruligani.gamesday.data.local.entities.GameSimpleEntity
import com.hafiztaruligani.gamesday.data.local.entities.RemoteKeyEntity
import com.hafiztaruligani.gamesday.data.remote.ApiService
import com.hafiztaruligani.gamesday.data.remote.dto.gamedetail.GameDetailResponse
import com.hafiztaruligani.gamesday.data.remote.dto.gameslist.GamesListResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okio.IOException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import java.util.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import retrofit2.HttpException
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
@Extensions(
    ExtendWith(MockitoExtension::class)
)
internal class GamesRepositoryImplTest {

    @Mock
    private lateinit var apiService: ApiService
    private lateinit var database: FakeGameDatabase
    private lateinit var gamesRepository: GamesRepositoryImpl

    private val query = "asd"
    private val page = 1
    private val pageSize = 20

    @BeforeEach
    fun setUp() {
        database = FakeGameDatabase()
        gamesRepository = GamesRepositoryImpl(apiService, database)
    }

    @Test
    fun `getGameDetail success`() = runTest {
        val id = 10
        Mockito.`when`(apiService.getGameDetail(id)).then { GameDetailResponse(id = id) }

        val data = gamesRepository.getGameDetail(id).first()
        assert(data?.id == id)
    }

    @Test
    fun `getGameDetail, network unavailable`() = runTest {
        val id = 10
        Mockito.`when`(apiService.getGameDetail(id)).then { throw IOException() }

        assertFailsWith(IOException::class){
            gamesRepository.getGameDetail(id)
        }
    }

    @Test
    fun `getGameDetail, HTTP exception`() = runTest {
        val id = 10
        Mockito.`when`(apiService.getGameDetail(id)).then { throw HttpExceptionBuilder.NotFoundException().exception }

        assertFailsWith(HttpException::class){
            gamesRepository.getGameDetail(id)
        }
    }

    @Test
    fun getGameDetailLocalCache() = runTest {
        val id = database.getValidGameDetail().id
        val data = gamesRepository.getGameDetailLocalCache(id).first()
        assertNotNull(data)
    }

    @Test
    fun `getGamesRemote success`() = runTest{
        val data = GamesListResponse()
        Mockito.`when`(apiService.getGames(query, page, pageSize))
            .then{ data }

        val result = gamesRepository.getGamesRemote(query, page, pageSize)
        assert( result == data )
    }

    @Test
    fun `getGamesRemote failed, network unavailable`() = runTest{
        Mockito.`when`(apiService.getGames(query, page, pageSize))
            .then{ throw IOException() }

        assertFailsWith(IOException::class) {
            gamesRepository.getGamesRemote(query, page, pageSize)
        }
    }

    @Test
    fun `getGamesRemote failed, HTTP exception, using cached data `() = runTest{
        Mockito.`when`(apiService.getGames(query, page, pageSize))
            .then{ throw HttpExceptionBuilder.NotFoundException().exception }

        assertFailsWith(HttpException::class) {
            gamesRepository.getGamesRemote(query, page, pageSize)
        }
    }

    @Test
    fun insertGameSimple()= runTest {
        val data = GameSimpleEntity(
            id = Random().nextInt(),
            name = "name",
            releaseDate = "releaseDate",
            rating = 1.1,
            image = "image",
            createdAt = 1231
        )
        gamesRepository.insertGameSimple(data)

        assertNotNull(database.gamesSimpleEntity.firstOrNull{it.id == data.id})
    }

    @Test
    fun getGamesPaged() {
    }

    @Test
    fun deleteGamesSimple() = runTest{
        gamesRepository.deleteGamesSimple()
        assert(database.gamesSimpleEntity.isEmpty())
    }

    @Test
    fun insertRemoteKey() = runTest{
        val data = RemoteKeyEntity(Random().nextInt().toString(),Random().nextInt())
        gamesRepository.insertRemoteKey(data)
    }

    @Test
    fun getRemoteKey() = runTest {
        val id = database.getValidRemoteKey().id
        val data = gamesRepository.getRemoteKey(id)
        assertNotNull(data)
    }

    @Test
    fun deleteRemoteKey() = runTest {
        val id = database.getValidRemoteKey().id
        gamesRepository.deleteRemoteKey(id)

        assertNull(
            database.remoteKeyEntity.firstOrNull{ it.id == id }
        )
    }

    @Test
    fun addFavorite() = runTest {
        val id = database.getValidGameDetail(false).id
        gamesRepository.addFavorite(id)
        assertTrue(
            database.gamesDetailEntity.first{ it.id == id }.favorite
        )
    }

    @Test
    fun deleteFavorite() = runTest {
        val id = database.getValidGameDetail(true).id
        gamesRepository.deleteFavorite(id)
        assertFalse(
            database.gamesDetailEntity.first{ it.id == id }.favorite
        )
    }

    @Test
    fun getFavoriteGames() = runTest {
        val data = gamesRepository.getFavoriteGames().first()
        val result = data.map { it.favorite }
        result.forEach {
            assertTrue(it)
        }
    }

}