package com.peter.restaurantsmap.framwork.datasource.responses

import com.google.gson.annotations.SerializedName

data class LocationResponse(
    @SerializedName("lat")
    val lat: Double?,

    @SerializedName("lng")
    val lng: Double?,
)
