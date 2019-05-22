package io.vinter.lostpet.ui.login

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.vinter.lostpet.R
import io.vinter.lostpet.network.form.LoginForm
import io.vinter.lostpet.ui.main.MainActivity
import io.vinter.lostpet.ui.register.RegisterActivity
import io.vinter.lostpet.utils.StyleApplicator
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        StyleApplicator.style(this)
        val viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        login_button.setOnClickListener {
            viewModel.getToken(LoginForm(login_login.text.toString(), login_password.text.toString()))
        }

        login_register_button.setOnClickListener {
            startActivityForResult(Intent(this, RegisterActivity::class.java), 22)
        }

        viewModel.userData.observe(this, Observer {
            if (it != null){
                val preferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
                preferences.edit().putString("token", it.token)
                        .putString("phone_number", it.phoneNumber)
                        .putString("name", it.name)
                        .putString("id", it.id)
                        .apply()
                if (it.pictureURL != null) preferences.edit().putString("pictureURL", it.pictureURL).apply()
                this.startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        })

    }
}
