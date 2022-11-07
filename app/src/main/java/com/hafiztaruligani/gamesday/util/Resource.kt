package com.hafiztaruligani.cryptoday.util


sealed class Resource <T> (){
    companion object{
        const val NETWORK_UNAVAILABLE = "Your network is unavailable"
        const val USING_CACHED_DATA = "Somethings wrong, this is cached data"
        const val DATA_NOT_FOUND = "Sorry, the data is not found"
        const val COMMON_ERROR = "Sorry, Somethings wrong"
    }

    class Success<T>(val data: T): Resource<T>()
    class Error<T>(var message: String = COMMON_ERROR, val data: T?=null): Resource<T>(){
        init {
             data?.let {
                 message = USING_CACHED_DATA
             }
        }
    }
    class Loading<T>(): Resource<T>()

}
