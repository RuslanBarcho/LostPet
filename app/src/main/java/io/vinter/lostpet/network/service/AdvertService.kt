package io.vinter.lostpet.network.service

import io.reactivex.Single
import io.vinter.lostpet.entity.Message
import io.vinter.lostpet.entity.advert.Advert
import io.vinter.lostpet.entity.advert.Detail
import io.vinter.lostpet.network.form.FilterForm
import okhttp3.MultipartBody
import retrofit2.http.*

interface AdvertService {
    @GET("/adverts")
    fun getAllAdverts(@Header("Authorization") token: String): Single<ArrayList<Advert>>

    @POST("/adverts/filtered")
    fun getFilteredAdverts(@Header("Authorization") token: String, @Body filterForm: FilterForm): Single<ArrayList<Advert>>

    @GET("/adverts/user-ads")
    fun getUserAdverts(@Header("Authorization") token: String): Single<ArrayList<Advert>>

    @GET("/adverts/advert/{advertId}")
    fun getAdvertById(@Header("Authorization") token: String, @Path("advertId") advertId: String): Single<Detail>

    @PUT("/adverts/advert/{advertId}")
    fun updateAdvert(@Header("Authorization") token: String, @Path("advertId") advertId: String, @Body advert: Advert): Single<Message>

    @DELETE("/adverts/advert/{advertId}")
    fun deleteAdvert(@Header("Authorization") token: String, @Path("advertId") advertId: String): Single<Message>

    @Multipart
    @POST("/adverts/create")
    fun createAdvert(@Header("Authorization") token: String, @Part files: List<MultipartBody.Part>, @Part json: MultipartBody.Part): Single<Message>
}