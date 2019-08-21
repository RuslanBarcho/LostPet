package io.vinter.lostpet.ui.create

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment

import io.vinter.lostpet.R
import io.vinter.lostpet.entity.advert.Advert
import io.vinter.lostpet.entity.advert.Location
import io.vinter.lostpet.ui.create.location.LocationPickFragment
import io.vinter.lostpet.ui.dialog.ProgressDialog
import io.vinter.lostpet.utils.PermissionManager
import io.vinter.lostpet.utils.config.StyleApplicator
import io.vinter.lostpet.utils.adapter.AddPictureRecyclerAdapter
import kotlinx.android.synthetic.main.activity_create.*

class CreateActivity : AppCompatActivity() {

    lateinit var viewModel: CreateViewModel
    private lateinit var adapter: AddPictureRecyclerAdapter

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
            if (viewModel.fileUri.value != null && create_title.text.toString() != "" && create_description.text.toString() != ""){
                val progress = ProgressDialog()
                progress.show(supportFragmentManager, "progress")
                viewModel.postAdvert(preferences.getString("token", "")!!, advert, this)
            } else Toast.makeText(this, getString(R.string.create_missing_data), Toast.LENGTH_SHORT).show()
        }

        create_back.setOnClickListener {
            this.finish()
        }

        create_location.setOnClickListener {
            val locationPicker = LocationPickFragment()
            if (supportFragmentManager.findFragmentByTag("locationPicker") == null) locationPicker.show(supportFragmentManager, "locationPicker")
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

        viewModel.location.observe(this, Observer {
            if (it != null){
                create_location.text = it.address
            }
        })

        viewModel.error.observe(this, Observer{
            if (it != null){
                val progress = supportFragmentManager.findFragmentByTag("progress") as DialogFragment?
                progress?.dismiss()
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                viewModel.error.postValue(null)
            }
        })
    }

    private fun getImageFromLibrary(){
        PermissionManager.requestFilePermission(this, this){
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 44)
        }
    }

    fun setLocation(location: Location) {
        viewModel.location.postValue(location)
    }

    private fun configureImageRecycler(files: ArrayList<Uri>){
        create_image_recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter =  AddPictureRecyclerAdapter(files, this){
            if (it == viewModel.fileUri.value!!.size) getImageFromLibrary()
            else {
                viewModel.fileUri.value!!.removeAt(it)
                adapter.removeItem(it, viewModel.fileUri.value!!)
            }
        }
        create_image_recycler.adapter = adapter
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
