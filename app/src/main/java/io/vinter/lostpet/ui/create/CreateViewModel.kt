package io.vinter.lostpet.ui.create

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.vinter.lostpet.entity.Message
import io.vinter.lostpet.entity.advert.Advert
import io.vinter.lostpet.entity.advert.Location
import io.vinter.lostpet.network.NetModule
import io.vinter.lostpet.network.service.AdvertService
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.File
import android.provider.MediaStore
import android.app.Activity
import android.content.Context
import io.vinter.lostpet.utils.RealPathUtil


class CreateViewModel: ViewModel(){

    var message = MutableLiveData<Message>()
    var error = MutableLiveData<String>()
    var fileUri = MutableLiveData<ArrayList<Uri>>()
    var location = MutableLiveData<Location>()
    lateinit var disposable: Disposable

    fun postAdvert(token: String, advert: Advert, context: Context){
        val uri = fileUri.value
        if (uri != null){
            var locationPart = null as MultipartBody.Part?
            val files = ArrayList<MultipartBody.Part>()
            uri.forEachIndexed {index, u -> files.add(prepareMultipart(u, index, context))}
            if (location.value != null) locationPart = MultipartBody.Part.createFormData("location", Gson().toJson(location.value))

            disposable = NetModule.retrofit.create(AdvertService::class.java)
                    .createAdvert("Bearer $token", files,
                            MultipartBody.Part.createFormData("json", Gson().toJson(advert)),
                            locationPart)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(message::postValue) {
                        Log.e("NETWORK ERROR", it.message)
                        if (it is HttpException) error.postValue(it.message()) else error.postValue("Problem with internet connection")
                    }
        }
    }

    private fun prepareMultipart(uri: Uri, pos: Int, context: Context): MultipartBody.Part{
        val file = File(RealPathUtil.getPathFromUri(context, uri))

        Log.i("FILENAME", file.name)
        return MultipartBody.Part.createFormData("image$pos", file.name, RequestBody.create(MediaType.parse("multipart/form-data"), file))
    }

}