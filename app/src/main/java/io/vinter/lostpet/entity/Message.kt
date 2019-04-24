package io.vinter.lostpet.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Message {

    @SerializedName("message")
    @Expose
    var message: String? = null

}