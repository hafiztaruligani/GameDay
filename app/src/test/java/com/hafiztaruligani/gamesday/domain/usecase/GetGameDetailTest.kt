package com.hafiztaruligani.gamesday.domain.usecase

import com.hafiztaruligani.cryptoday.util.Resource
import com.hafiztaruligani.gamesday.HttpExceptionBuilder
import com.hafiztaruligani.gamesday.data.local.entities.GameDetailEntity
import com.hafiztaruligani.gamesday.domain.model.GameDetail
import com.hafiztaruligani.gamesday.domain.repository.GamesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
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
@Extensions(value = [
    ExtendWith(MockitoExtension::class)
])
internal class GetGameDetailTest{

    @Mock
    private lateinit var gamesRepository: GamesRepository

    private lateinit var getGameDetail: GetGameDetail
    private val gameEntity = GameDetailEntity(
        id = 1,
        name = "default",
        releaseDate = "default",
        rating = "default",
        image = "default",
        description = "default",
        company = "default",
        playCount = 2,
        favorite = true,
    )

    @BeforeEach
    fun setup(){
        getGameDetail = GetGameDetail(gamesRepository)
    }

    @Test
    fun success()= runTest{
        Mockito.`when`(gamesRepository.getGameDetail(gameEntity.id)).then{ flow { emit(gameEntity)  } }

        val resultFlow = getGameDetail.invoke(gameEntity.id)
        assertFlowItem(resultFlow)

        assert(resultFlow.last() is Resource.Success)

        val data = (resultFlow.last() as Resource.Success).data
        assert( data::class == gameEntity.toGameDetail()::class )
        assert( data.id == gameEntity.toGameDetail().id )
    }

    @Test
    fun `no connection, cached data Unavailable`() = runTest {
        Mockito.`when`(gamesRepository.getGameDetail(gameEntity.id)).then{ throw IOException() }
        Mockito.`when`(gamesRepository.getGameDetailLocalCache(gameEntity.id)).then{ flow<GameDetailEntity?> { emit(null) } }

        val resultFlow = getGameDetail.invoke(gameEntity.id)
        assertFlowItem(resultFlow)

        assert(resultFlow.last() is Resource.Error)

        val data = (resultFlow.last() as Resource.Error).data
        assertNull( data )

    }

    @Test
    fun `no connection, cached data Available`() = runTest {
        Mockito.`when`(gamesRepository.getGameDetail(gameEntity.id)).then{ throw IOException() }
        Mockito.`when`(gamesRepository.getGameDetailLocalCache(gameEntity.id)).then{ flow { emit(gameEntity) } }

        val resultFlow = getGameDetail.invoke(gameEntity.id)
        assertFlowItem(resultFlow)

        assert(resultFlow.last() is Resource.Error)

        val data = (resultFlow.last() as Resource.Error).data
        assertNotNull( data )
        assert(data?.id == gameEntity.id)
    }

    @Test
    fun `HTTP exception, cached data Unavailable`() = runTest {
        Mockito.`when`(gamesRepository.getGameDetail(gameEntity.id)).then{ throw HttpExceptionBuilder.NotFoundException().exception }
        Mockito.`when`(gamesRepository.getGameDetailLocalCache(gameEntity.id)).then{ flow<GameDetailEntity?> { emit(null) } }

        val resultFlow = getGameDetail.invoke(gameEntity.id)
        assertFlowItem(resultFlow)

        assert(resultFlow.last() is Resource.Error)

        val data = (resultFlow.last() as Resource.Error).data
        assertNull( data )
    }

    @Test
    fun `HTTP exception, cached data Available`() = runTest {
        Mockito.`when`(gamesRepository.getGameDetail(gameEntity.id)).then{ throw HttpExceptionBuilder.NotFoundException().exception }
        Mockito.`when`(gamesRepository.getGameDetailLocalCache(gameEntity.id)).then{ flow { emit(gameEntity) } }

        val resultFlow = getGameDetail.invoke(gameEntity.id)
        assertFlowItem(resultFlow)

        assert(resultFlow.last() is Resource.Error)

        val data = (resultFlow.last() as Resource.Error).data
        assertNotNull( data )
        assert(data?.id == gameEntity.id)
    }

    private fun assertFlowItem(flow: Flow<Resource<GameDetail>>)= runTest{
        // only Loading and (Success or Error)
        assertEquals(2, flow.count())
        // first loading item
        assertTrue(flow.first() is Resource.Loading)

        val lastItem = flow.last()
        if(lastItem is Resource.Success || (lastItem is Resource.Error && lastItem.data !== null)){

            val data = try {
                (flow.last() as Resource.Success).data
            } catch (e: java.lang.Exception){
                (flow.last() as Resource.Error).data!!
            }

            data.apply {
                assert( id == gameEntity.id )
                assert( name == gameEntity.name )
                assert( releaseDate == gameEntity.releaseDate )
                assert( rating == gameEntity.rating )
                assert( image == gameEntity.image )
                assert( description == gameEntity.description )
                assert( company == gameEntity.company )
                assert( playCount == gameEntity.playCount.toString() )
                assert( favorite == gameEntity.favorite )
            }

        }
    }

}