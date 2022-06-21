package com.peter.restaurantsmap.framwork.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.ApiException
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.TravelMode
import com.peter.restaurantsmap.BuildConfig
import com.peter.restaurantsmap.models.Location
import com.peter.restaurantsmap.models.Place
import com.peter.restaurantsmap.repository.LocationRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import com.google.maps.model.DirectionsRoute
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.collect


@HiltViewModel
class MainViewModel @Inject constructor(private val repo: LocationRepo) : ViewModel() {
    private val _locations = MutableLiveData<List<Location>>()
    val locations: MutableLiveData<List<Location>>
        get() = _locations

    private val _places = MutableLiveData<List<Place>>()
    val places: MutableLiveData<List<Place>>
        get() = _places

    private val _selectedPlace = MutableLiveData<Place>()
    val selectedPlace: MutableLiveData<Place>
        get() = _selectedPlace

    private val _placeVisibility = MutableLiveData<Int>()
    val placeVisibility: MutableLiveData<Int>
        get() = _placeVisibility

    private val _currentLocation = MutableLiveData<Location?>()
    val currentLocation: LiveData<Location?>
        get() = _currentLocation

    private val _path = MutableLiveData<List<List<LatLng>>>()
    val path: MutableLiveData<List<List<LatLng>>>
        get() = _path

    init {
        _selectedPlace.value = Place(
            id = "0",
            isOpen = false,
            photoReference = "",
            rating = 0.0f,
            name = "",
            longitude = 0.0,
            latitude = 0.0,
            vicinity = ""
        )
        viewModelScope.launch {
            repo.getAll(_locations)
        }
//           viewModelScope.launch {
//               val location = Location()
//               location.name = "Grand Kadri Hotel By Cristal Lebanon"
//               location.latitude = 33.85148430277257
//               location.longitude = 35.895525763213946
//               repo.add(location)
//
//               location.name = "Germanos - Pastry"
//               location.latitude = 33.85217073479985
//               location.longitude = 35.89477838111461
//               repo.add(location)
//
//               location.name = "Malak el Tawook"
//               location.latitude = 33.85334017189446
//               location.longitude = 35.89438946093824
//               repo.add(location)
//
//               location.name = "Z Burger House"
//               location.latitude = 33.85454300475094
//               location.longitude = 35.894561122304474
//               repo.add(location)
//
//               location.name = "Collège Oriental"
//               location.latitude = 33.85129821373707
//               location.longitude = 35.89446263654391
//               repo.add(location)
//
//               location.name = "VERO MODA"
//               location.latitude = 33.85048738635312
//               location.longitude = 35.89664059012788
//               repo.add(location)
//           }
    }

    fun setCurrent(lat: Double, lng: Double) {
        val location = Location()
        location.name = "My Location"
        location.latitude = lat
        location.longitude = lng
        _currentLocation.value = location
    }

    fun setSelectedPlace(id: String) {
        _selectedPlace.value = _places.value!!.find { it.id == id }
    }

    fun setVisibility(v: Int) {
        _placeVisibility.value = v
    }

    fun getNearbyRestaurants() {
        Log.i(
            "place_location",
            "${_currentLocation.value!!.latitude},${_currentLocation.value!!.longitude}"
        )
        viewModelScope.launch {
            repo.getRestaurants("${_currentLocation.value!!.latitude},${_currentLocation.value!!.longitude}")
                .collect {
                    Log.i("place_size", it.size.toString())
                    for (d in it)
                        Log.i("place_name", d.name)
                    _places.value = it
                }
        }

    }

    fun getDirection() {
        viewModelScope.launch {
            repo.getDirection(
                origin = "${_currentLocation.value!!.latitude},${_currentLocation.value!!.longitude}",
                destination = "${_selectedPlace.value!!.latitude},${_selectedPlace.value!!.longitude}"
            )
                .collect {
                    _path.value = it
                }
        }

    }


}


