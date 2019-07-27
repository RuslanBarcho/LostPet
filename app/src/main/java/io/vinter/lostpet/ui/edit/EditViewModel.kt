package io.vinter.lostpet.ui.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.vinter.lostpet.entity.Message
import io.vinter.lostpet.entity.advert.Advert
import io.vinter.lostpet.network.NetModule
import io.vinter.lostpet.network.service.AdvertService
import retrofit2.HttpException

class EditViewModel: ViewModel(){

    var message = MutableLiveData<Pair<Int, Message>>()
    var error = MutableLiveData<String>()

    fun updateAdvert(token: String, advert: Advert){
        NetModule.retrofit.create(AdvertService::class.java)
                .updateAdvert("Bearer $token", advert.id!!, advert)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({msg -> message.postValue(Pair(20, msg))},  {
                    if (it is HttpException) error.postValue(it.message()) else error.postValue("Problem with internet connection")
                })
    }

    fun deleteAdvert(token: String, advertId: String){
        NetModule.retrofit.create(AdvertService::class.java)
                .deleteAdvert("Bearer $token", advertId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({msg -> message.postValue(Pair(21, msg))},  {
                    if (it is HttpException) error.postValue(it.message()) else error.postValue("Problem with internet connection")
                })
    }
}