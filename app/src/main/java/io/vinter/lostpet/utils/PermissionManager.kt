package io.vinter.lostpet.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.tbruyelle.rxpermissions2.RxPermissions

object PermissionManager{
    fun requestFilePermission(context: Context, activity: Activity, void: () -> Unit){
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            val rxPermissions = RxPermissions(activity)
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe { granted ->
                        if (granted) {
                            void()
                        }
                    }
        } else {
            void()
        }
    }
}