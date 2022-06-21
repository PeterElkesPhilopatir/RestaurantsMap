package com.peter.restaurantsmap.repository

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.peter.restaurantsmap.framwork.datasource.DirectionSource
import com.peter.restaurantsmap.framwork.datasource.SearchSource
import com.peter.restaurantsmap.models.Location
import com.peter.restaurantsmap.models.Place
import com.peter.restaurantsmap.service.FirestoreLocation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface LocationRepo {
    suspend fun getAll(liveData: MutableLiveData<List<Location>>)
    suspend fun add(location: Location): Boolean
    suspend fun getRestaurants(location: String): Flow<List<Place>>
    suspend fun getDirection(origin: String,destination : String): Flow<List<List<LatLng>>>
}

class LocationRepoImpl @Inject constructor(
    private val store: FirestoreLocation,
    private val source: SearchSource,
    private val directionSource : DirectionSource
) : LocationRepo {
    override suspend fun getAll(liveData: MutableLiveData<List<Location>>) =
        store.getLocations(liveData)
    override suspend fun add(location: Location) = store.addLocation(location)
    override suspend fun getRestaurants(location: String): Flow<List<Place>> =
        source.searchForRestaurant(location)

    override suspend fun getDirection(origin: String,destination : String): Flow<List<List<LatLng>>> =
        directionSource.getDirection(origin,destination)

}