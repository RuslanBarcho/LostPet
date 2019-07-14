package io.vinter.lostpet.ui.list

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import io.vinter.lostpet.entity.advert.AdvertResponse
import io.vinter.lostpet.network.NetModule
import io.vinter.lostpet.network.form.FilterForm
import io.vinter.lostpet.network.service.AdvertService
import retrofit2.HttpException

class AllPetsViewModel : ViewModel() {

    var adverts = MutableLiveData<AdvertResponse>()
    var addictionAdverts = MutableLiveData<AdvertResponse>()
    var error = MutableLiveData<String>()
    var loading: Boolean = false
    private var state = State()

    fun getAllAdverts(token: String, fromItem: String?) {
        loading = true
        NetModule.retrofit.create(AdvertService::class.java)
                .getAllAdverts("Bearer $token")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({result ->
                    processResponse(result, fromItem != null, RequestType.NORMAL, null, null)
                }, { e ->
                    if (e is HttpException) error.postValue(e.message()) else error.postValue("Problem with internet connection")
                })
    }

    fun getFilteredAdverts(token: String, filter: FilterForm, fromItem: String?){
        loading = true
        NetModule.retrofit.create(AdvertService::class.java)
                .getFilteredAdverts("Bearer $token", filter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({result ->
                    processResponse(result, fromItem != null, RequestType.FILTER, filter, null)
                }, {e ->
                    if (e is HttpException) error.postValue(e.message()) else error.postValue("Problem with internet connection")
                })
    }

    fun searchAdverts(token: String, query: String, fromItem: String?) {
        loading = true
        NetModule.retrofit.create(AdvertService::class.java)
                .searchAdverts("Bearer $token", query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    processResponse(result, fromItem != null, RequestType.SEARCH, null, query)
                }, { e ->
                    if (e is HttpException) error.postValue(e.message()) else error.postValue("Problem with internet connection")
                })
    }

    private fun processResponse(advertResponse: AdvertResponse, addMore: Boolean, requestType: RequestType, filter: FilterForm?, query: String? ){
        loading = false
        if (addMore) addictionAdverts.postValue(advertResponse)
        else adverts.postValue(advertResponse)
        state.lastRequestType = requestType
        if (requestType == RequestType.FILTER) state.lastFilter = filter
        else if (requestType == RequestType.SEARCH) state.lastSearchQuery = query
    }

    fun refresh(token: String, fromItem: String?){
        when (state.lastRequestType){
            RequestType.NORMAL -> getAllAdverts(token, fromItem)
            RequestType.FILTER -> getFilteredAdverts(token, state.lastFilter!!, fromItem)
            RequestType.SEARCH -> searchAdverts(token, state.lastSearchQuery!!, fromItem)
            else -> getAllAdverts(token, fromItem)
        }
    }
}
