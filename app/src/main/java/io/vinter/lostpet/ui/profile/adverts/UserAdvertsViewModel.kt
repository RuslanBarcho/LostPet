package io.vinter.lostpet.ui.profile.adverts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.vinter.lostpet.entity.advert.Advert
import io.vinter.lostpet.entity.advert.AdvertResponse
import io.vinter.lostpet.network.NetModule
import io.vinter.lostpet.network.service.AdvertService
import retrofit2.HttpException
import java.util.ArrayList

class UserAdvertsViewModel: ViewModel(){

    var adverts = MutableLiveData<AdvertResponse>()
    var error = MutableLiveData<String>()

    fun getUserAdverts(token: String) {
        NetModule.retrofit.create(AdvertService::class.java)
                .getUserAdverts("Bearer $token")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adverts::postValue) {e ->
                    if (e is HttpException) error.postValue(e.toString()) else error.postValue("Problem with internet connection")
                }
    }
}