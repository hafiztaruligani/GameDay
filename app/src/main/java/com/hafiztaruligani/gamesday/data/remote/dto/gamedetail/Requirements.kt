package com.hafiztaruligani.gamesday.data.remote.dto.gamedetail

import com.google.gson.annotations.SerializedName

data class Requirements(

	@field:SerializedName("minimum")
	val minimum: String? = null,

	@field:SerializedName("recommended")
	val recommended: String? = null
)