package com.peter.restaurantsmap.framwork.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.model.DirectionsRoute
import com.peter.restaurantsmap.R
import com.peter.restaurantsmap.databinding.FragmentMainBinding
import com.peter.restaurantsmap.framwork.datasource.Utils.AD_UNIT_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.properties.Delegates


@AndroidEntryPoint
class MainFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel
    private var isPermissionGranted by Delegates.notNull<Boolean>()
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        /** setup Map **/
        isPermissionGranted = isLocationPermissionGranted()

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.onResume()
        binding.mapView.getMapAsync(this)
        /** End setup Map**/
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient((context as ContextWrapper).baseContext as Activity)

        viewModel.apply {
            locations.observe(viewLifecycleOwner) { list ->
                if (list.isNotEmpty()) {
                    Log.i("location_SIZE", list.size.toString())
                    binding.sourceLocationEdt.setAdapter(
                        ArrayAdapter(
                            (context as ContextWrapper).baseContext as Activity,
                            R.layout.support_simple_spinner_dropdown_item,
                            list.map { it.name })
                    )
                    refreshMap()
                }
            }
            places.observe(viewLifecycleOwner) {
                if (it.isNotEmpty())
                    refreshMap()
            }

            selectedPlace.observe(viewLifecycleOwner) {
                if (it.id != "0") {
                    viewModel.setVisibility(View.VISIBLE)
                    viewModel.getDirection()
                } else viewModel.setVisibility(View.GONE)
            }

            path.observe(viewLifecycleOwner) {
                    refreshMap()
            }


        }
        return binding.root
    }

    private fun isLocationPermissionGranted(): Boolean {

        return if (ActivityCompat.checkSelfPermission(
                (context as ContextWrapper).baseContext as Activity,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                (context as ContextWrapper).baseContext as Activity,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                (context as ContextWrapper).baseContext as Activity,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1000
            )
            binding.mapView.onResume()
            binding.mapView.getMapAsync(this)
            false

        } else {
            true
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        mMap = map

        if (isPermissionGranted) {
            map.isMyLocationEnabled = true
        }
        getLastKnownLocation()
        mMap.setOnMapClickListener {
            viewModel.setVisibility(View.GONE)
            updateMarkers()
        }


    }

    private fun updateMarkers() {
        /**DRAW CURRENT LOCATION**/
        lifecycleScope.launch {
            if (viewModel.currentLocation.value != null)
                drawMarker(
                    LatLng(
                        viewModel.currentLocation.value!!.latitude,
                        viewModel.currentLocation.value!!.longitude
                    ),
                    viewModel.currentLocation.value!!.name,
                    "0"
                )
        }

        /**DRAW LOCATIONS LOCATION**/
        lifecycleScope.launch {
            if (viewModel.locations.value != null)
                for (d in viewModel.locations.value!!) {
                    drawMarker(
                        LatLng(
                            d.latitude,
                            d.longitude
                        ),
                        d.name,
                        "0"
                    )
                }
        }

        /**DRAW PLACES LOCATION**/
        lifecycleScope.launch {
            if (viewModel.places.value != null)
                for (d in viewModel.places.value!!) {
                    drawMarker(
                        LatLng(
                            d.latitude,
                            d.longitude
                        ),
                        d.name,
                        d.id
                    )
                }
        }
    }

    private fun drawMarker(latLng: LatLng, title: String, id: String) {

        mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(title)
                .snippet(id)

        )
        mMap.setOnMarkerClickListener { marker -> // on marker click we are getting the title of our marker
            // which is clicked and displaying it in a toast message.
            if (marker.snippet!! != "0") {
                marker.snippet?.let { viewModel.setSelectedPlace(it) }
            }
            false
        }
    }

    private fun moveCamera(latLng: LatLng, zoom: Float, title: String) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
        drawMarker(latLng, title, "0")
    }

    @SuppressLint("MissingPermission")
    fun getLastKnownLocation() {
        if (isPermissionGranted) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        moveCamera(
                            LatLng(location.latitude, location.longitude),
                            15.0f,
                            "My Location"
                        )
                        viewModel.setCurrent(location.latitude, location.longitude)
                    }

                }
        }

    }

    private fun drawDirection() {
        if (viewModel.path.value != null) {
            if (viewModel.path.value!!.isNotEmpty())
                for (i in viewModel.path.value!!)
                    mMap.addPolyline(PolylineOptions().addAll(i).color(Color.RED))

        }
    }

    private fun refreshMap() {
        mMap.clear()
        drawDirection()
        updateMarkers()
    }


}