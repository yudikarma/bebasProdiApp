package com.haekal.bebasprodiapp.data

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var uid:String="",
    var email:String = "",
    var password:String = "",
    var name:String = "",
    var address:String = "",
    var kelas:String = "",
    var urlPict:String = "",
    var rule:String = "",
    var nohp :String = "",
    var nim :String = ""
){

    @Exclude
    fun toMap(): Map<String,Any?>{
        return  mapOf(
            "uid" to uid,
            "email" to email,
            "password" to password,
            "name" to name,
            "address" to address,
            "kelas" to kelas,
            "urlPict" to urlPict,
            "rule" to rule,
            "nohp" to nohp,
            "nim" to nim
        )
    }
}
