package io.vinter.lostpet.network.service

import io.reactivex.Single
import io.vinter.lostpet.entity.Message
import io.vinter.lostpet.entity.advert.Advert
import io.vinter.lostpet.entity.advert.AdvertResponse
import io.vinter.lostpet.entity.advert.Detail
import io.vinter.lostpet.network.form.FilterForm
import okhttp3.MultipartBody
import retrofit2.http.*

interface AdvertService {
    @GET("/posts")
    fun getAllAdverts(@Header("Authorization") token: String): Single<AdvertResponse>

    @GET("/posts/search")
    fun searchAdverts(@Header("Authorization") token: String, @Query("q") q: String): Single<AdvertResponse>

    @POST("/posts/filtered")
    fun getFilteredAdverts(@Header("Authorization") token: String, @Body filterForm: FilterForm): Single<AdvertResponse>

    @GET("/posts/user-ads")
    fun getUserAdverts(@Header("Authorization") token: String): Single<AdvertResponse>

    @GET("/posts/post/{advertId}")
    fun getAdvertById(@Header("Authorization") token: String, @Path("advertId") advertId: String): Single<Detail>

    @PUT("/posts/post/{advertId}")
    fun updateAdvert(@Header("Authorization") token: String, @Path("advertId") advertId: String, @Body advert: Advert): Single<Message>

    @DELETE("/posts/post/{advertId}")
    fun deleteAdvert(@Header("Authorization") token: String, @Path("advertId") advertId: String): Single<Message>

    @Multipart
    @POST("/posts/create")
    fun createAdvert(@Header("Authorization") token: String,
                     @Part files: List<MultipartBody.Part>,
                     @Part json: MultipartBody.Part,
                     @Part location: MultipartBody.Part?): Single<Message>
}