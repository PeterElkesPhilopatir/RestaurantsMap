package com.peter.restaurantsmap.framwork.datasource.service


import com.peter.restaurantsmap.framwork.datasource.responses.directions_responses.MainResponseDirections
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface DirectionService {

    @GET("/maps/api/directions/json")
    fun getDirectionsAsync(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("mode") mode: String,
        @Query("key") key: String
    ):
            Deferred<MainResponseDirections>
}