package io.vinter.lostpet.entity.advert

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Advert: Serializable {

    @SerializedName("_id")
    @Expose
    var id: String? = null
    @SerializedName("owner")
    @Expose
    var owner: String? = null
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
    var puctureUrl: ArrayList<String>? = null

    constructor()

    constructor(animalType: String?, advertType: String?, advertTitle: String?, advertDescription: String?) {
        this.animalType = animalType
        this.advertType = advertType
        this.advertTitle = advertTitle
        this.advertDescription = advertDescription
    }

    constructor(detail: Detail){
        this.advertTitle = detail.advertTitle
        this.advertType = detail.advertType
        this.advertTitle = detail.advertTitle
        this.animalType = detail.animalType
        this.advertDescription = detail.advertDescription
        this.owner = detail.owner!!.id
        this.advertStatus = detail.advertStatus
        this.puctureUrl = detail.pictureUrl
        this.id = detail.id
        this.v = detail.v
    }

}