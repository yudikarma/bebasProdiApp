package com.haekal.bebasprodiapp.utils

import android.content.Context
import android.widget.ImageView
import com.haekal.bebasprodiapp.R
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng


object MapUtil {
    fun setCamera(latLng: LatLng?, googleMap: GoogleMap?) {
        if (latLng != null && googleMap != null) {
            val cameraPosition = CameraPosition.Builder().target(latLng).zoom(13f).build()
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
    }

    fun loadStaticMapIntoImageView(context: Context, latitude: Double, longitude: Double, imageView: ImageView) {
        val location = "$latitude,$longitude"
        val url = "https://maps.googleapis.com/maps/api/staticmap?center=" + location +
                "&zoom=17.0&size=1000x400&maptype=roadmap&markers=color:blue%7C" + location +
                "&key=" + context.getString(R.string.maps_key)
        println(url)
        Glide.with(context).load(url).into(imageView)
    }

}