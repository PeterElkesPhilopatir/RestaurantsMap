package com.peter.restaurantsmap.framwork.datasource.responses.directions_responses

import com.google.gson.annotations.SerializedName

data class PolyLineResponse (
    @SerializedName("points")
    val points : String?,
)