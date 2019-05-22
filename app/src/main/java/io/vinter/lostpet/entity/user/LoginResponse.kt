package io.vinter.lostpet.entity.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginResponse {

    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("token")
    @Expose
    var token: String? = null
    @SerializedName("phone_number")
    @Expose
    var phoneNumber: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("pictureURL")
    @Expose
    var pictureURL: String? = null

}