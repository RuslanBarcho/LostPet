package io.vinter.lostpet.ui.register

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.vinter.lostpet.entity.Message
import io.vinter.lostpet.network.NetModule
import io.vinter.lostpet.network.form.RegisterForm
import io.vinter.lostpet.network.service.UserService
import retrofit2.HttpException

class RegisterViewModel:ViewModel(){

    var message = MutableLiveData<Message>()
    var error = MutableLiveData<String>()

    fun register(registerForm: RegisterForm){
        NetModule.retrofit.create(UserService::class.java)
                .createUser(registerForm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(message::postValue) {
                    if (it is HttpException) error.postValue(it.message()) else error.postValue("Problem with internet connection")
                }
    }
}