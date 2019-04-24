package io.vinter.lostpet.ui.list

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import java.util.ArrayList

import io.vinter.lostpet.entity.advert.Advert
import io.vinter.lostpet.network.NetModule
import io.vinter.lostpet.network.form.FilterForm
import io.vinter.lostpet.network.service.AdvertService
import retrofit2.HttpException

class AllPetsViewModel : ViewModel() {

    var adverts = MutableLiveData<ArrayList<Advert>>()
    var error = MutableLiveData<String>()

    fun getAllAdverts(token: String) {
        NetModule.retrofit.create(AdvertService::class.java)
                .getAllAdverts("Bearer $token")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adverts::postValue) {e ->
                    if (e is HttpException) error.postValue(e.message()) else error.postValue("Problem with internet connection")
                }
    }

    fun getFilteredAdverts(token: String, filter: FilterForm){
        NetModule.retrofit.create(AdvertService::class.java)
                .getFilteredAdverts("Bearer $token", filter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adverts::postValue) {e ->
                    if (e is HttpException) error.postValue(e.message()) else error.postValue("Problem with internet connection")
                }
    }
}
