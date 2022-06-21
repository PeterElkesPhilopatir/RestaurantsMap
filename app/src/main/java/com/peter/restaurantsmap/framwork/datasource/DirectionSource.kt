package com.peter.restaurantsmap.framwork.datasource

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.peter.restaurantsmap.BuildConfig
import com.peter.restaurantsmap.framwork.datasource.Utils.TRAVEL_MODE
import com.peter.restaurantsmap.framwork.datasource.Utils.parseStepsToPath
import com.peter.restaurantsmap.framwork.datasource.service.DirectionService
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import javax.inject.Inject

interface DirectionSource {


    suspend fun getDirection(
        origin: String, destination: String
    ): Flow<List<List<LatLng>>>
}

class DirectionSourceImpl @Inject constructor(
    private val directionService : DirectionService
) : DirectionSource {


    override suspend fun getDirection(origin: String, destination: String) = flow {
        try {
            val response =
                directionService.getDirectionsAsync(
                    origin = origin,
                    destination = destination,
                    mode = TRAVEL_MODE,
                    key = BuildConfig.MAPS_API_KEY
                ).await()
            try {
                if (response.status.equals("OK")) {
                    emit(parseStepsToPath(response.routes!![0].legs!![0].steps!!))
                }

            } catch (e: Exception) {
                Log.e("direction_error", e.message.toString())
                e.printStackTrace()
            }
        } catch (e: Throwable) {
            Log.e("direction_error", e.message.toString())
            e.printStackTrace()

        }
    }.flowOn(IO)

}






