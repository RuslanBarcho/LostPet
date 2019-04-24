package io.vinter.lostpet.ui.create

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.ArrayAdapter
import android.widget.Toast

import io.vinter.lostpet.R
import io.vinter.lostpet.entity.advert.Advert
import io.vinter.lostpet.utils.StyleApplicator
import io.vinter.lostpet.utils.adapter.AddPictureRecyclerAdapter
import kotlinx.android.synthetic.main.activity_create.*

class CreateActivity : AppCompatActivity() {

    private lateinit var viewModel: CreateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        StyleApplicator.style(this)

        viewModel = ViewModelProviders.of(this).get(CreateViewModel::class.java)
        val preferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)

        val petAdapter = ArrayAdapter.createFromResource(this, R.array.types, R.layout.spinner_pets_item)
        val postAdapter = ArrayAdapter.createFromResource(this, R.array.post_types, R.layout.spinner_post_item)
        petAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        postAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinner_pet_type.adapter = petAdapter
        spinner_post_type.adapter = postAdapter

        if (viewModel.fileUri.value == null) viewModel.fileUri.postValue(ArrayList())

        create_button_done.setOnClickListener {
            var animalType = "cat"
            var advertType= "missed"
            if (spinner_pet_type.selectedItemPosition == 1) animalType = "dog"
            when (spinner_post_type.selectedItemPosition){
                1 -> advertType = "found"
                2 -> advertType = "good-hands"
            }
            val advert = Advert(animalType, advertType, create_title.text.toString(), create_description.text.toString())
            viewModel.postAdvert(this, preferences.getString("token", "")!!, advert)
        }

        create_button_cancel.setOnClickListener {
            this.finish()
        }

        create_back.setOnClickListener {
            this.finish()
        }

        viewModel.message.observe(this, Observer {
            if (it != null){
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                setResult(11)
                this.finish()
            }
        })

        viewModel.fileUri.observe(this, Observer {
            if (it != null){
                configureImageRecycler(it)
            }
        })

        viewModel.error.observe(this, Observer{
            if (it != null){
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                viewModel.error.postValue(null)
            }
        })
    }

    private fun getImageFromLibrary(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 44)
    }

    private fun configureImageRecycler(files: ArrayList<Uri>){
        create_image_recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        create_image_recycler.adapter = AddPictureRecyclerAdapter(files, this) {
            getImageFromLibrary()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        when(requestCode){
            44 -> {
                if (data != null){
                    val list = ArrayList<Uri>()
                    val currentList = viewModel.fileUri.value
                    if (currentList != null) {
                        list.addAll(currentList)
                    }
                    list.add(data.data!!)
                    viewModel.fileUri.postValue(list)
                }
            }
        }
    }

}
