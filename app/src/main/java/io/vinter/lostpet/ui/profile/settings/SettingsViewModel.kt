package io.vinter.lostpet.ui.profile.settings

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.content.Context
import android.net.Uri
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.vinter.lostpet.entity.Message
import io.vinter.lostpet.entity.advert.User
import io.vinter.lostpet.network.NetModule
import io.vinter.lostpet.network.service.UserService
import io.vinter.lostpet.utils.RealPathUtil
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.File

@SuppressLint("CheckResult")
class SettingsViewModel: ViewModel(){

    var userData = MutableLiveData<User>()
    var pictureUrl = MutableLiveData<Message>()
    var error = MutableLiveData<String>()

    fun editUser(token: String, user: User){
        NetModule.retrofit.create(UserService::class.java)
                .editUser("Bearer $token", user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userData::postValue) {
                    if (it is HttpException) error.postValue(it.message()) else error.postValue("Problem with internet connection")
                }
    }

    fun uploadImage(token: String, context: Context, uri: Uri){
        NetModule.retrofit.create(UserService::class.java)
                .uploadUserPicture("Bearer $token", prepareMultipart(uri, context))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pictureUrl::postValue) {
                    if (it is HttpException) error.postValue(it.message()) else error.postValue("Problem with internet connection")
                }
    }

    private fun prepareMultipart(uri: Uri, context: Context): MultipartBody.Part{
        val realPath = RealPathUtil.getPathFromUri(context, uri)
        val file = File(realPath)
        return MultipartBody.Part.createFormData("image", file.name, RequestBody.create(MediaType.parse("multipart/form-data"), file))
    }
}