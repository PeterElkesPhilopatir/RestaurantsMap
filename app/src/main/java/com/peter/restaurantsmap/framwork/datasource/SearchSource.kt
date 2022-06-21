package com.peter.restaurantsmap.framwork.datasource

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.peter.restaurantsmap.BuildConfig
import com.peter.restaurantsmap.framwork.datasource.Utils.RADIUS
import com.peter.restaurantsmap.framwork.datasource.Utils.TRAVEL_MODE
import com.peter.restaurantsmap.framwork.datasource.Utils.TYPE
import com.peter.restaurantsmap.framwork.datasource.Utils.parseStepsToPath
import com.peter.restaurantsmap.framwork.datasource.Utils.parseToPlainObject
import com.peter.restaurantsmap.framwork.datasource.service.ApiService
import com.peter.restaurantsmap.framwork.datasource.service.DirectionService
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import com.peter.restaurantsmap.models.Place

interface SearchSource {
    suspend fun searchForRestaurant(
        location: String
    ): Flow<List<Place>>


}

class SearchSourceImpl @Inject constructor(
    private val api: ApiService,
) : SearchSource {

    override suspend fun searchForRestaurant(
        location: String,
    ): Flow<List<Place>> =
        flow {
            try {
                val response =
                    api.getNearbyRestaurantAsync(
                        location = location,
                        type = TYPE,
                        radius = RADIUS,
                        key = BuildConfig.MAPS_API_KEY
                    ).await()
                try {
                    if (response.status.equals("OK")) {
                        val list = response.results
                        emit(list!!.map {
                            it.parseToPlainObject()
                        })
                    }

                } catch (e: Exception) {
                    Log.e("place_error", e.message.toString())
                    e.printStackTrace()

                }
            } catch (e: Throwable) {
                Log.e("place_error", e.message.toString())
                e.printStackTrace()

            }

        }.flowOn(IO)

}






