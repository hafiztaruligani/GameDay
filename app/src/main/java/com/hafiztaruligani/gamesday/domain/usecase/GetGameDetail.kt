package com.hafiztaruligani.gamesday.domain.usecase

import com.hafiztaruligani.cryptoday.util.Resource
import com.hafiztaruligani.gamesday.domain.model.GameDetail
import com.hafiztaruligani.gamesday.domain.repository.GamesRepository
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetGameDetail @Inject constructor( private val gamesRepository: GamesRepository ) {

    suspend operator fun invoke(gameId: Int) = flow<Resource<GameDetail>> {

        emit(Resource.Loading())

        // if error, get local data using this function
        suspend fun localCache(errorMessage: String){
            gamesRepository.getGameDetailLocalCache(gameId).collect{
                if(it!=null) emit(
                    Resource.Error( errorMessage, data = it.toGameDetail() )
                )
                else emit( Resource.Error( errorMessage ) )
            }
        }

        /*
         try getting data from repository
         if repository throw exception, will be catch here
         */
        try {

            gamesRepository.getGameDetail(gameId).collect { data->
                data?.let {
                    emit( Resource.Success( it.toGameDetail() ) )
                }
            }

        } catch (e: HttpException) {
            localCache(Resource.COMMON_ERROR)
        } catch (e: IOException){
            localCache(Resource.NETWORK_UNAVAILABLE)
        }


    }

}