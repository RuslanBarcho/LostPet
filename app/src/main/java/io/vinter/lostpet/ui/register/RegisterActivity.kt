package io.vinter.lostpet.ui.register

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.vinter.lostpet.R
import io.vinter.lostpet.network.form.RegisterForm
import io.vinter.lostpet.utils.config.StyleApplicator
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        StyleApplicator.style(this)
        val viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)

        register.setOnClickListener {
            if ((register_phone.text.toString() != "") and (register_password.text.toString() != "") and (register_name.text.toString() != "")) {
                if (register_password.text.toString() == register_confirm_password.text.toString()) {
                    viewModel.register(RegisterForm(register_phone.text.toString(), register_name.text.toString(), register_password.text.toString()))
                } else {
                    Toast.makeText(this, getString(R.string.error_password_match), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, getString(R.string.error_fields_required), Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.message.observe(this, Observer {
            if (it != null){
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                this.finish()
            }
        })
    }
}
