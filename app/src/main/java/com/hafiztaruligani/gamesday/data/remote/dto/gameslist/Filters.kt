package com.hafiztaruligani.gamesday.data.remote.dto.gameslist

import com.google.gson.annotations.SerializedName

data class Filters(

	@field:SerializedName("years")
	val years: List<YearsItem?>? = null
)