package com.hafiztaruligani.gamesday.data.remote.dto.gameslist

import com.google.gson.annotations.SerializedName

data class StoresItem(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("store")
	val store: Store? = null
)