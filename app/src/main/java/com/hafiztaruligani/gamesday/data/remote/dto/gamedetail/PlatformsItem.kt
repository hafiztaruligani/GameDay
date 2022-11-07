package com.hafiztaruligani.gamesday.data.remote.dto.gamedetail

import com.google.gson.annotations.SerializedName

data class PlatformsItem(

	@field:SerializedName("requirements")
	val requirements: Requirements? = null,

	@field:SerializedName("released_at")
	val releasedAt: String? = null,

	@field:SerializedName("platform")
	val platform: Platform? = null
)