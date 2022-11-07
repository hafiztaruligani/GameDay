package com.hafiztaruligani.gamesday.data.remote.dto.gamedetail

import com.google.gson.annotations.SerializedName

data class ParentPlatformsItem(

	@field:SerializedName("platform")
	val platform: Platform? = null
)