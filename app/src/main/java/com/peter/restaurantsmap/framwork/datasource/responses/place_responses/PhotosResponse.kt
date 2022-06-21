package com.peter.restaurantsmap.framwork.datasource.responses.place_responses

import com.google.gson.annotations.SerializedName

data class PhotosResponse(
    @SerializedName("photo_reference")
    val photo_reference: String?,
)

