package com.hafiztaruligani.gamesday.data.remote.dto.gameslist

import com.google.gson.annotations.SerializedName

data class RequirementsRu(

	@field:SerializedName("minimum")
	val minimum: String? = null,

	@field:SerializedName("recommended")
	val recommended: String? = null
)