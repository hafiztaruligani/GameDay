package com.hafiztaruligani.gamesday.data.remote.dto.gamedetail

import com.google.gson.annotations.SerializedName

data class StoresItem(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("store")
	val store: Store? = null,

	@field:SerializedName("url")
	val url: String? = null
)