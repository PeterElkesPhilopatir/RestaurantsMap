package com.peter.restaurantsmap.framwork.datasource.service


import com.peter.restaurantsmap.framwork.datasource.responses.place_responses.MainResponsePlaces
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("maps/api/place/nearbysearch/json")
    fun getNearbyRestaurantAsync(
        @Query("location") location: String,
        @Query("type") type: String,
        @Query("radius") radius: Int,
        @Query("key") key: String
    ):
            Deferred<MainResponsePlaces>

}