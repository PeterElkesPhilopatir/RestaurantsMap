package com.peter.restaurantsmap.models

class Place(
    var name: String,
    var latitude: Double,
    var longitude: Double,
    var id: String,
    var vicinity: String,
    var rating: Float,
    var photoReference: String?,
    var isOpen : Boolean?
)