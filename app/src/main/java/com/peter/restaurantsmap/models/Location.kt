package com.peter.restaurantsmap.models

 open class Location (
     var name: String = "",
     var latitude: Double= 0.0,
     var longitude: Double= 0.0,
 ) {
     fun hash(): HashMap<String, Any> {
         return hashMapOf(
             "name" to this.name,
             "latitude" to this.latitude,
             "longitude" to this.longitude
         )
     }
 }