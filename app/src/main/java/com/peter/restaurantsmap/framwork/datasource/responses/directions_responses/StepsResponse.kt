package com.peter.restaurantsmap.framwork.datasource.responses.directions_responses

import com.google.gson.annotations.SerializedName
import com.peter.restaurantsmap.framwork.datasource.responses.LocationResponse

data class StepsResponse (


    @SerializedName("start_location")
    val start_location : LocationResponse?,

    @SerializedName("end_location")
    val end_location : LocationResponse?,

    @SerializedName("polyline")
    val polyline : PolyLineResponse?,

    )