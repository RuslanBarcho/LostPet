package io.vinter.lostpet.ui.create.location

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.tbruyelle.rxpermissions2.RxPermissions

import io.vinter.lostpet.R
import io.vinter.lostpet.ui.create.CreateActivity
import kotlinx.android.synthetic.main.fragment_location_pick.*
import java.io.IOException
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
                       (activity as CreateActivity).setLocation(address[0].getAddressLine(0), location)
                       dismiss()
                   } catch (e: IOException){ }
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

    private fun getLocationPermission(googleMap: GoogleMap) {
        if (ContextCompat.checkSelfPermission(context!!, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    private fun initMap() {
        val mapView = this.fragmentManager!!.findFragmentById(R.id.map) as SupportMapFragment
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

    private fun moveCamera(latLng: LatLng, zoom: Float, googleMap: GoogleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setContentView(R.layout.fragment_filter)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
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
