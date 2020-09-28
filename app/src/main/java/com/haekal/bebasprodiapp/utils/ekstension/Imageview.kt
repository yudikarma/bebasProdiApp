package com.haekal.bebasprodiapp.utils.ekstension

import android.widget.ImageView
import com.haekal.bebasprodiapp.utils.AppConstans
import com.bumptech.glide.Glide


inline fun ImageView.loadFromUrl(url:String){
    Glide.with(this)
        .setDefaultRequestOptions(AppConstans.placeholderRequest)
        .load(url).into(this)

}