package com.peter.restaurantsmap.framwork.datasource.responses.place_responses

import com.google.gson.annotations.SerializedName

data class PlaceResponse(
    @SerializedName("place_id")
    val place_id: String?,

    @SerializedName("rating")
    val rating: Float?,

    @SerializedName("geometry")
    val geometry: GeometryResponse?,

    @SerializedName("photos")
    val photos: List<PhotosResponse>,

    @SerializedName("vicinity")
    val vicinity: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("opening_hours")
    val opening_hours: Hours?

)
