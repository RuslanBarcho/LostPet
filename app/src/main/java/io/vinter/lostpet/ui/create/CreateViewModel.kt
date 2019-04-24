package io.vinter.lostpet.ui.create

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.vinter.lostpet.entity.Message
import io.vinter.lostpet.entity.advert.Advert
import io.vinter.lostpet.network.NetModule
import io.vinter.lostpet.network.service.AdvertService
import io.vinter.lostpet.utils.RealPathUtil
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.File

class CreateViewModel: ViewModel(){

    var message = MutableLiveData<Message>()
    var error = MutableLiveData<String>()
    var fileUri = MutableLiveData<ArrayList<Uri>>()

    fun postAdvert(context: Context, token: String, advert: Advert){
        val uri = fileUri.value
        if (uri != null){
            val files = ArrayList<MultipartBody.Part>()
            uri.forEachIndexed {index, u -> files.add(prepareMultipart(u, context, index))}
            NetModule.retrofit.create(AdvertService::class.java)
                    .createAdvert("Bearer $token", files,
                            MultipartBody.Part.createFormData("json", Gson().toJson(advert)))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(message::postValue) {
                        if (it is HttpException) error.postValue(it.message()) else error.postValue("Problem with internet connection")
                    }
        }
    }

    private fun prepareMultipart(uri: Uri, context: Context, pos: Int): MultipartBody.Part{
        val realPath = RealPathUtil.getRealPathFromURI_API19(context, uri)
        val file = File(realPath)
        return MultipartBody.Part.createFormData("image$pos", file.name, RequestBody.create(MediaType.parse("multipart/form-data"), file))
    }

}