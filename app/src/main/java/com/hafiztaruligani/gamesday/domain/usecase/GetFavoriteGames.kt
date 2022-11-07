package com.hafiztaruligani.gamesday.domain.usecase

import com.hafiztaruligani.cryptoday.util.Resource
import com.hafiztaruligani.gamesday.domain.repository.GamesRepository
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetFavoriteGames @Inject constructor(private val gamesRepository: GamesRepository) {

    suspend operator fun invoke() = flow {

        emit(Resource.Loading())

        /*
         try getting data from repository
         if repository throw exception, will be catch here
         */
        try {
            gamesRepository.getFavoriteGames().collect { list ->
                emit(
                    Resource.Success(
                        list.map { it.toGameSimple() }
                    )
                )
            }
        } catch (e: HttpException) {
            emit(Resource.Error(message = Resource.COMMON_ERROR))
        } catch (e: IOException){
            emit(Resource.Error(message = Resource.NETWORK_UNAVAILABLE))
        }
    }
}