package com.hafiztaruligani.gamesday.data.remote

import com.hafiztaruligani.gamesday.data.remote.dto.gamedetail.GameDetailResponse
import com.hafiztaruligani.gamesday.data.remote.dto.gameslist.GamesListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET ("games")
    suspend fun getGames(
        @Query("search") query: String,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): GamesListResponse

    @GET ("games/{id}")
    suspend fun getGameDetail(@Path("id") gameId: Int): GameDetailResponse

}
