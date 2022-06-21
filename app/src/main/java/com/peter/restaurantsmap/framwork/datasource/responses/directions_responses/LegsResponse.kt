package com.peter.restaurantsmap.framwork.datasource.responses.directions_responses

import com.google.gson.annotations.SerializedName
import com.peter.restaurantsmap.framwork.datasource.responses.LocationResponse

data class LegsResponse(
    @SerializedName("start_address")
    val start_address: String?,

    @SerializedName("end_address")
    val end_address: String?,

    @SerializedName("start_location")
    val start_location: LocationResponse?,

    @SerializedName("end_location")
    val end_location: LocationResponse?,

    @SerializedName("steps")
    val steps: List<StepsResponse>?,

    )