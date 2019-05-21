package io.vinter.lostpet.entity.advert

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class User {

    @SerializedName("_id")
    @Expose
    var id: String? = null
    @SerializedName("phone_number")
    @Expose
    var phoneNumber: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("pictureURL")
    @Expose
    var pictureURL: String? = null

}