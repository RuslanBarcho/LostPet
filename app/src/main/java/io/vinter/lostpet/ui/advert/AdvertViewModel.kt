package io.vinter.lostpet.ui.advert

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.vinter.lostpet.entity.Message
import io.vinter.lostpet.entity.advert.Detail
import io.vinter.lostpet.network.NetModule
import io.vinter.lostpet.network.service.AdvertService
import io.vinter.lostpet.network.service.UserService
import retrofit2.HttpException

@SuppressLint("CheckResult")
class AdvertViewModel: ViewModel(){

    var advert = MutableLiveData<Detail>()
    var message = MutableLiveData<Message>()
    var error = MutableLiveData<String>()

    fun getAdvertDetail(token: String, advertId: String){
        NetModule.retrofit.create(AdvertService::class.java)
                .getAdvertById("Bearer $token", advertId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(advert::postValue) {
                    if (it is HttpException) error.postValue(it.message) else error.postValue("Problem with internet connection")
                }
    }

    fun addToFavorites(token: String, advertId: String){
        NetModule.retrofit.create(UserService::class.java)
                .addToFavs("Bearer $token", advertId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(message::postValue) {
                    if (it is HttpException) error.postValue(it.message()) else error.postValue("Problem with internet connection")
                }
    }

    fun deleteFromFavs(token: String, id: String){
        Log.i("ID ADVERT TO DELETE", id)
        NetModule.retrofit.create(UserService::class.java)
                .deleteFromFavs("Bearer $token", id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(message::postValue) {
                    if (it is HttpException) error.postValue(it.message()) else error.postValue("Problem with internet connection")
                }
    }
}