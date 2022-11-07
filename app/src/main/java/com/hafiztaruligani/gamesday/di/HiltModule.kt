package com.hafiztaruligani.gamesday.di

import android.content.Context
import com.hafiztaruligani.gamesday.BuildConfig
import com.hafiztaruligani.gamesday.data.local.AppDatabase
import com.hafiztaruligani.gamesday.data.local.GamesDao
import com.hafiztaruligani.gamesday.data.remote.ApiService
import com.hafiztaruligani.gamesday.data.repository.GamesRepositoryImpl
import com.hafiztaruligani.gamesday.domain.repository.GamesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    @Provides
    @Singleton
    fun provideApi(): ApiService {

        // add interceptor to add authentication
        val authenticator = Interceptor { chain ->
            var default = chain.request()
            val apiKey = BuildConfig.API_KEY
            val url = default.url.newBuilder().addQueryParameter("key", apiKey).build()
            default = default.newBuilder().url(url).build()
            chain.proceed(default)
        }

        // logging
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val client = OkHttpClient().newBuilder()
            .addNetworkInterceptor(logger)
            .addInterceptor(authenticator)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()

        return Retrofit
            .Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase{
        return AppDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideGamesDao(
        appDatabase: AppDatabase
    ) = appDatabase.getGamesDao()

    @Provides
    @Singleton
    fun provideGamesRepository(
        apiService: ApiService,
        gamesDao: GamesDao
    ): GamesRepository =
        GamesRepositoryImpl(
            apiService,
            gamesDao
        )


}