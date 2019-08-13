package io.vinter.lostpet.ui.create.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.DialogFragment
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.tbruyelle.rxpermissions2.RxPermissions

import io.vinter.lostpet.R
import io.vinter.lostpet.ui.create.CreateActivity
import kotlinx.android.synthetic.main.fragment_location_pick.*
import java.lang.Exception
import java.util.*

class LocationPickFragment : DialogFragment(), OnMapReadyCallback {

    var location: LatLng? = null

    override fun onMapReady(map: GoogleMap?) {
       if (map != null) {
           getLocationPermission(map)
           location_select.setOnClickListener {
               val location = map.cameraPosition.target
               Thread {
                   try {
                       val address = Geocoder(context, Locale.getDefault()).getFromLocation(location.latitude, location.longitude, 1)
                       (activity as CreateActivity).setLocation(io.vinter.lostpet.entity.advert.Location(address[0].getAddressLine(0), location.latitude, location.longitude))
                       dismiss()
                   } catch (e: Exception){
                       activity?.runOnUiThread{Toast.makeText(context, "Невозможно определить адрес", Toast.LENGTH_SHORT).show()}
                   }
               }.start()
           }
       }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_location_pick, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
    }

    @SuppressLint("CheckResult")
    private fun getLocationPermission(googleMap: GoogleMap) {
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            val rxPermissions = RxPermissions(activity!!)
            rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    .subscribe { granted ->
                        if (granted) {
                            getDeviceLocation(googleMap)
                        }
                    }
        } else {
            getDeviceLocation(googleMap)
        }
    }

    @SuppressLint("ResourceType")
    private fun initMap() {
        val mapView = this.fragmentManager!!.findFragmentById(R.id.map) as SupportMapFragment
        val locationButton = mapView.view?.findViewById(2) as ImageView?
        locationButton?.setImageResource(R.drawable.ic_location_find)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) locationButton?.foreground = resources.getDrawable(R.drawable.ripple_transparent)
        locationButton?.setBackgroundResource(R.drawable.background_white_circle)
        locationButton?.elevation = 12f

        val params = locationButton?.layoutParams as RelativeLayout.LayoutParams?
        params?.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
        params?.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        params?.setMargins(0, 0, getInDp(10f), getInDp(10f))

        locationButton?.layoutParams = params

        mapView.getMapAsync(this)
    }

    private fun getDeviceLocation(googleMap: GoogleMap) {
        val mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity!!)
        try {
            val location = mFusedLocationProviderClient.lastLocation
            location.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    val currentLocation = task.result as Location
                    moveCamera(LatLng(currentLocation.latitude, currentLocation.longitude), 15f, googleMap)
                    googleMap.isMyLocationEnabled = true
                }
            }
        } catch (e: SecurityException) {

        }
    }

    private fun getInDp(dp: Float): Int{
        val r = context?.resources
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r?.displayMetrics).toInt()
    }

    private fun moveCamera(latLng: LatLng, zoom: Float, googleMap: GoogleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setContentView(R.layout.fragment_filter)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        return dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        val mapView = this.fragmentManager!!.findFragmentById(R.id.map) as SupportMapFragment
        if (fragmentManager != null){
            fragmentManager!!.beginTransaction().remove(mapView).commitAllowingStateLoss()
        }
    }

}
