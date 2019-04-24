package io.vinter.lostpet.ui.login

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.vinter.lostpet.entity.user.LoginResponse
import io.vinter.lostpet.network.NetModule
import io.vinter.lostpet.network.form.LoginForm
import io.vinter.lostpet.network.service.UserService
import retrofit2.HttpException

class LoginViewModel: ViewModel(){

    var userData = MutableLiveData<LoginResponse>()
    var error = MutableLiveData<String>()

    fun getToken(form: LoginForm){
        NetModule.retrofit.create(UserService::class.java)
                .getToken(form)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ loginResponse ->
                    userData.postValue(loginResponse)
                }, { e ->
                    if (e is HttpException) error.postValue(e.message()) else error.postValue("Problem with internet connection")
                })
    }
}