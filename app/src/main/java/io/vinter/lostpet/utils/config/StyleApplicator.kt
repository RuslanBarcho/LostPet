package io.vinter.lostpet.utils.config

import android.app.Activity
import android.os.Build
import android.view.View

import io.vinter.lostpet.R

/**
 * Класс со статическим методом, реализующий применение стиля к активности в зависимости от API level устройства
 */
object StyleApplicator {
    /**
     * Метод применения стиля
     * @param activity активность, к которой необходимо применить стиль
     */
    fun style(activity: Activity) {
        val view = activity.window.decorView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.systemUiVisibility = view.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            activity.window.statusBarColor = activity.applicationContext.getColor(R.color.colorPrimary)
        } else {
            activity.window.statusBarColor = activity.resources.getColor(R.color.colorAccent)
        }
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1){
//            view.systemUiVisibility = view.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
//            activity.window.navigationBarColor = activity.getColor(config.navBarColor)
//        }
    }
}
