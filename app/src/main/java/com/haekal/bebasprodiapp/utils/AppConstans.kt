package com.haekal.bebasprodiapp.utils

import com.haekal.bebasprodiapp.R
import com.bumptech.glide.request.RequestOptions

object AppConstans {
    enum class RULE{DOSEN,MAHASISWA}

    val placeholderRequest = RequestOptions.placeholderOf(R.drawable.default_avatar)

    val PARCEL_IS_CREATED = "DIBUAT"
    val PARCEL_IS_PICK_UP = "DIANGKUT"
    val PARCEL_IS_DELIVER = "DIKIRIM"
    val PARCEL_IS_ARRIVE = "TIBA"
    val PARCEL_IS_DONE = "SELESAI"
}