package com.hafiztaruligani.gamesday.domain.usecase

import com.hafiztaruligani.gamesday.domain.repository.GamesRepository
import javax.inject.Inject

class SetFavorite @Inject constructor(private val gamesRepository: GamesRepository) {

    suspend operator fun invoke(gameId: Int, value: Boolean){
        if (value) gamesRepository.addFavorite(gameId)
        else gamesRepository.deleteFavorite(gameId)
    }

}