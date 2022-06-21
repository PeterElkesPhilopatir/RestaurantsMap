package com.peter.restaurantsmap.framwork.datasource.responses.place_responses

import com.google.gson.annotations.SerializedName

data class Hours (
    @SerializedName("open_now")
    val open_now: Boolean?
)