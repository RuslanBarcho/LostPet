package io.vinter.lostpet.ui.advert

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.vinter.lostpet.entity.advert.Detail
import io.vinter.lostpet.network.NetModule
import io.vinter.lostpet.network.service.AdvertService
import retrofit2.HttpException

class AdvertViewModel: ViewModel(){

    var advert = MutableLiveData<Detail>()
    var error = MutableLiveData<String>()

    fun getAdvertDetail(token: String, advertId: String){
        NetModule.retrofit.create(AdvertService::class.java)
                .getAdvertById("Bearer $token", advertId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(advert::postValue) {
                    if (it is HttpException) error.postValue(it.message()) else error.postValue("Problem with internet connection")
                }
    }
}