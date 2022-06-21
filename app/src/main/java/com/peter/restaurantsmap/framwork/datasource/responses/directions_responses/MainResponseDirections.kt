package com.peter.restaurantsmap.framwork.datasource.responses.directions_responses

import com.google.gson.annotations.SerializedName

data class MainResponseDirections (
    @SerializedName("status")
    val status : String?,

    @SerializedName("routes")
    val routes : List<RouteResponse>?
)
