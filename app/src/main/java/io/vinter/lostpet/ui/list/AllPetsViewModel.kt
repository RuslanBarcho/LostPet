package io.vinter.lostpet.ui.list

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import io.vinter.lostpet.entity.advert.AdvertResponse
import io.vinter.lostpet.network.NetModule
import io.vinter.lostpet.network.form.FilterForm
import io.vinter.lostpet.network.service.AdvertService
import retrofit2.HttpException

@SuppressLint("CheckResult")
class AllPetsViewModel : ViewModel() {

    var adverts = MutableLiveData<AdvertResponse>()
    var addictionAdverts = MutableLiveData<AdvertResponse>()
    var fragmentState = MutableLiveData<State.FragmentState>()
    var error = MutableLiveData<String>()
    var loading: Boolean = false
    private var state = State()

    fun getAllAdverts(token: String, fromItem: String?) {
        loading = true
        NetModule.retrofit.create(AdvertService::class.java)
                .getAllAdverts("Bearer $token", fromItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({result ->
                    processResponse(result, fromItem != null, RequestType.NORMAL, null, null)
                }, { e ->
                    processError(e, fromItem == null)
                })
    }

    fun getFilteredAdverts(token: String, filter: FilterForm, fromItem: String?){
        loading = true
        NetModule.retrofit.create(AdvertService::class.java)
                .getFilteredAdverts("Bearer $token", filter, fromItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({result ->
                    processResponse(result, fromItem != null, RequestType.FILTER, filter, null)
                }, {e ->
                    processError(e, fromItem == null)
                })
    }

    fun searchAdverts(token: String, query: String, fromItem: String?) {
        loading = true
        NetModule.retrofit.create(AdvertService::class.java)
                .searchAdverts("Bearer $token", query, fromItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    processResponse(result, fromItem != null, RequestType.SEARCH, null, query)
                }, { e ->
                    processError(e, fromItem == null)
                })
    }

    private fun processResponse(advertResponse: AdvertResponse, addMore: Boolean, requestType: RequestType, filter: FilterForm?, query: String? ){
        loading = false
        fragmentState.postValue(State.FragmentState.NORMAL)
        if (addMore) addictionAdverts.postValue(advertResponse)
        else adverts.postValue(advertResponse)
        state.lastRequestType = requestType
        if (requestType == RequestType.FILTER) state.lastFilter = filter
        else if (requestType == RequestType.SEARCH) state.lastSearchQuery = query
    }

    private fun processError(e: Throwable, needError: Boolean){
        loading = false
        if (e is HttpException) error.postValue(e.message()) else error.postValue("Problem with internet connection")
        if (needError) fragmentState.postValue(State.FragmentState.ERROR)
    }

    fun refresh(token: String, fromItem: String?, showLoader: Boolean){
        if (showLoader) fragmentState.postValue(State.FragmentState.LOADING)
        when (state.lastRequestType){
            RequestType.NORMAL -> getAllAdverts(token, fromItem)
            RequestType.FILTER -> getFilteredAdverts(token, state.lastFilter!!, fromItem)
            RequestType.SEARCH -> searchAdverts(token, state.lastSearchQuery!!, fromItem)
            else -> getAllAdverts(token, fromItem)
        }
    }
}
