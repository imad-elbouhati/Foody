package com.imadev.foody.model


data class Address(
    var city: String? = "",
    var state: String? = "",
    var country: String? = "",
    var address: String? = "",
    val latLng: LatLng = LatLng()

)

data class LatLng(val latitude: Double = 0.0, val longitude: Double = 0.0)
