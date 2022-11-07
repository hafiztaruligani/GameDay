package com.hafiztaruligani.gamesday.data.remote.dto.gameslist

import com.google.gson.annotations.SerializedName

data class PlatformsItem(

	@field:SerializedName("requirements_ru")
	val requirementsRu: Any? = null,

	@field:SerializedName("requirements_en")
	val requirementsEn: Any? = null,

	@field:SerializedName("released_at")
	val releasedAt: String? = null,

	@field:SerializedName("platform")
	val platform: Platform? = null
)