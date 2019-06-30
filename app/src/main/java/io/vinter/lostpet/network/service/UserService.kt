package io.vinter.lostpet.network.service

import io.reactivex.Single
import io.vinter.lostpet.entity.Message
import io.vinter.lostpet.entity.advert.Advert
import io.vinter.lostpet.entity.advert.AdvertResponse
import io.vinter.lostpet.entity.advert.User
import io.vinter.lostpet.entity.user.LoginResponse
import io.vinter.lostpet.network.form.LoginForm
import io.vinter.lostpet.network.form.RegisterForm
import okhttp3.MultipartBody
import retrofit2.http.*

interface UserService {
    @POST("user/obtain-token")
    fun getToken(@Body loginForm: LoginForm): Single<LoginResponse>

    @POST("user/create")
    fun createUser(@Body registerForm: RegisterForm): Single<Message>

    @POST("user/favs/add")
    fun addToFavs(@Header("Authorization") token: String, @Query("advertId") id: String): Single<Message>

    @DELETE("user/favs")
    fun deleteFromFavs(@Header("Authorization") token: String, @Query("advertId") id: String): Single<Message>

    @GET("user/favs")
    fun getFavs(@Header("Authorization") token: String): Single<AdvertResponse>

    @PUT("user/edit")
    fun editUser(@Header("Authorization") token: String, @Body user:User): Single<User>

    @Multipart
    @POST("user/picture")
    fun uploadUserPicture(@Header("Authorization") token: String,@Part image: MultipartBody.Part): Single<Message>
}