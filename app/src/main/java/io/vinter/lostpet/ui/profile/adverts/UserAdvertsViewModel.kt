package io.vinter.lostpet.ui.profile.adverts

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.vinter.lostpet.entity.advert.AdvertResponse
import io.vinter.lostpet.network.NetModule
import io.vinter.lostpet.network.service.AdvertService
import io.vinter.lostpet.utils.config.FragmentState
import retrofit2.HttpException

@SuppressLint("CheckResult")
class UserAdvertsViewModel: ViewModel(){

    var adverts = MutableLiveData<AdvertResponse>()
    var state = MutableLiveData<FragmentState>()
    var error = MutableLiveData<String>()

    fun getUserAdverts(token: String) {
        state.postValue(FragmentState.LOADING) // переделать при введении пагинации
        NetModule.retrofit.create(AdvertService::class.java)
                .getUserAdverts("Bearer $token")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({adverts ->
                    this.adverts.postValue(adverts)
                    state.postValue(FragmentState.NORMAL)
                }, {e ->
                    if (e is HttpException) error.postValue(e.toString()) else error.postValue("Problem with internet connection")
                    state.postValue(FragmentState.ERROR)
                })
    }
}