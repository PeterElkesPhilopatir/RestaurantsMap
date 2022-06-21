package com.peter.restaurantsmap.framwork.datasource.responses.place_responses

import com.google.gson.annotations.SerializedName
import com.peter.restaurantsmap.framwork.datasource.responses.LocationResponse

data class GeometryResponse(

    @SerializedName("location")
    val location: LocationResponse?,
    )
