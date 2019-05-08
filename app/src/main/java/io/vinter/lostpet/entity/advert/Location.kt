package io.vinter.lostpet.entity.advert

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Location {

    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("latitude")
    @Expose
    var latitude: Double? = null
    @SerializedName("longitude")
    @Expose
    var longitude: Double? = null

    constructor()

    constructor(address: String, latitude: Double, longitude: Double){
        this.address = address
        this.latitude = latitude
        this.longitude = longitude
    }
}