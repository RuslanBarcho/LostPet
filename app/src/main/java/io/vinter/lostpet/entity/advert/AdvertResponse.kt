package io.vinter.lostpet.entity.advert

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AdvertResponse{
    @SerializedName("count")
    @Expose
    var count: Int? = null
    @SerializedName("adverts")
    @Expose
    var adverts: ArrayList<Advert>? = null
}