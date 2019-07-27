package io.vinter.lostpet.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.vinter.lostpet.R
import io.vinter.lostpet.ui.login.LoginActivity

class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        val preferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        if (preferences.getString("token", "") == "") {
            this.startActivity(Intent(this, LoginActivity::class.java))
            this.finish()
        } else {
            this.startActivity(Intent(this, MainActivity::class.java))
            this.finish()
        }
    }
}
