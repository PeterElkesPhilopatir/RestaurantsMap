package com.peter.restaurantsmap.framwork.datasource.responses.directions_responses

import com.google.gson.annotations.SerializedName

data class RouteResponse(
    @SerializedName("legs")
    val legs: List<LegsResponse>?,
)