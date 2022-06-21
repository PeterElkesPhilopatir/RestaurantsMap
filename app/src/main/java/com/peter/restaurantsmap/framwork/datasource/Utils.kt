package com.peter.restaurantsmap.framwork.datasource

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import com.peter.restaurantsmap.framwork.datasource.responses.directions_responses.StepsResponse
import com.peter.restaurantsmap.framwork.datasource.responses.place_responses.PlaceResponse
import com.peter.restaurantsmap.models.Place

object Utils {
    const val BASE_URL = "https://maps.googleapis.com/"
    const val RADIUS = 2000
    const val TYPE = "restaurant"
    const val MAX_WIDTH = 200
    const val MAX_HEIGHT = 200
    const val TRAVEL_MODE = "driving"
    const val OK_RESPONSE = "OK"
    const val AD_UNIT_ID = "ca-app-pub-2559564046715323/2204952129"

    fun PlaceResponse.parseToPlainObject() = Place(
        id = this.place_id ?: "",
        latitude = this.geometry!!.location!!.lat ?: 0.0,
        longitude = this.geometry.location!!.lng ?: 0.0,
        name = this.name ?: "",
        vicinity = this.vicinity ?: "",
        photoReference = this.photos?.let { it[0].photo_reference } ?: "",
//        photoReference = let {this.photos[0].photo_reference ?: ""},
        rating = this.rating ?: 0.0f,
        isOpen = this.opening_hours?.let { this.opening_hours.open_now },

//        isOpen = this.opening_hours!!.open_now ?: false
    )

    fun parseStepsToPath(steps: List<StepsResponse>): List<List<LatLng>> {
        val path: MutableList<List<LatLng>> = ArrayList()
        for (i in steps) {
            val points = i.polyline!!.points
            path.add(PolyUtil.decode(points))
        }
        return path

    }

}