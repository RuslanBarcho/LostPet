package io.vinter.lostpet.ui.profile.favs

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.vinter.lostpet.entity.advert.Advert
import io.vinter.lostpet.entity.advert.AdvertResponse
import io.vinter.lostpet.network.NetModule
import io.vinter.lostpet.network.service.UserService
import retrofit2.HttpException
import java.util.ArrayList

class FavoritesViewModel: ViewModel(){
    var adverts = MutableLiveData<AdvertResponse>()
    var error = MutableLiveData<String>()

    fun getFavorites(token: String) {
        NetModule.retrofit.create(UserService::class.java)
                .getFavs("Bearer $token")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adverts::postValue) {e ->
                    if (e is HttpException) error.postValue(e.toString()) else error.postValue("Problem with internet connection")
                }
    }
}