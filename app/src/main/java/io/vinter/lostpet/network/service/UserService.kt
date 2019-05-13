package io.vinter.lostpet.network.service

import io.reactivex.Single
import io.vinter.lostpet.entity.Message
import io.vinter.lostpet.entity.advert.Advert
import io.vinter.lostpet.entity.advert.User
import io.vinter.lostpet.entity.user.LoginResponse
import io.vinter.lostpet.network.form.LoginForm
import io.vinter.lostpet.network.form.RegisterForm
import retrofit2.http.*

interface UserService {
    @POST("user/obtain-token")
    fun getToken(@Body loginForm: LoginForm): Single<LoginResponse>

    @POST("user/create")
    fun createUser(@Body registerForm: RegisterForm): Single<Message>

    @POST("user/favs/add")
    fun addToFavs(@Header("Authorization") token: String, @Body advert: Advert): Single<Message>

    @GET("user/favs")
    fun getFavs(@Header("Authorization") token: String): Single<ArrayList<Advert>>

    @PUT("user/edit")
    fun editUser(@Header("Authorization") token: String, @Body user:User): Single<Message>
}