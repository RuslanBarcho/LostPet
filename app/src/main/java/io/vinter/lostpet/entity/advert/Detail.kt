package io.vinter.lostpet.entity.advert

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Detail: Serializable {

    @SerializedName("_id")
    @Expose
    var id: String? = null
    @SerializedName("owner")
    @Expose
    var owner: User? = null
    @SerializedName("animalType")
    @Expose
    var animalType: String? = null
    @SerializedName("advertStatus")
    @Expose
    var advertStatus: Boolean? = null
    @SerializedName("advertType")
    @Expose
    var advertType: String? = null
    @SerializedName("advertTitle")
    @Expose
    var advertTitle: String? = null
    @SerializedName("advertDescription")
    @Expose
    var advertDescription: String? = null
    @SerializedName("__v")
    @Expose
    var v: Int? = null
    @SerializedName("pictureURL")
    @Expose
    var pictureUrl: ArrayList<String>? = null

}