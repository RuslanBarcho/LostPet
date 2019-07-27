package io.vinter.lostpet.ui.edit

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.vinter.lostpet.R
import io.vinter.lostpet.entity.advert.Advert
import io.vinter.lostpet.utils.config.StyleApplicator
import kotlinx.android.synthetic.main.activity_edit.*

class EditActivity : AppCompatActivity() {

    private lateinit var viewModel: EditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        StyleApplicator.style(this)
        viewModel = ViewModelProviders.of(this).get(EditViewModel::class.java)
        val preferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val advert = intent.getSerializableExtra("data") as Advert

        if (savedInstanceState == null){
            edit_title.setText(advert.advertTitle)
            edit_description.setText(advert.advertDescription)
        }

        edit_button_done.setOnClickListener {
            if (edit_title.text.toString() != "" && edit_description.text.toString() != ""){
                advert.advertTitle = edit_title.text.toString()
                advert.advertDescription = edit_description.text.toString()
                viewModel.updateAdvert(preferences.getString("token", "")!!, advert)
            }
        }

        edit_button_delete.setOnClickListener {
            viewModel.deleteAdvert(preferences.getString("token", "")!!, advert.id!!)
        }

        edit_back.setOnClickListener {
            finish()
        }
        edit_button_cancel.setOnClickListener {
            finish()
        }

        viewModel.message.observe(this, Observer {
            if (it != null){
                Toast.makeText(this, it.second.message, Toast.LENGTH_SHORT).show()
                setResult(it.first)
                finish()
            }
        })

    }
}
