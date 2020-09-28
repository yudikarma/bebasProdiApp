package com.haekal.bebasprodiapp.data

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ServerValue
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.sql.Timestamp

@IgnoreExtraProperties
@Parcelize class BebasProdi(
    var id:String = "",
    var freeKey: String = "",
    var freeLab:String = "",
    var freeLibrary:String= "",
    var freKompen:String= "",
    var buktiFreeKey:String= "",
    var buktiFreeLab:String= "",
    var buktiFreeLibrary:String= "",
    var buktiFreeKompen:String= "",
    var remark:String= "",
    var idUser:String= "",
    var complete:String= "",
    var catatan:String= "",
    var createOn:String= "",
    var kelas:String= "",
    var name:String= "",
    var address:String = "",
    var urlPict:String = "",
    var nohp :String = "",
    var nim :String = "",
    var email:String = "",
    var timestamp:Map<String,String> = ServerValue.TIMESTAMP
    ):Parcelable{




    @Exclude
    fun toMap(): Map<String,Any?>{
        return  mapOf(
            "id" to id,
            "freeKey" to freeKey,
            "freeLab" to freeLab,
            "freeLibrary" to freeLibrary,
            "freKompen" to freKompen,
            "buktiFreeKey" to buktiFreeKey,
            "buktiFreeLab" to buktiFreeLab,
            "buktiFreeLibrary" to buktiFreeLibrary,
            "buktiFreeKompen" to buktiFreeKompen,
            "remark" to remark,
            "idUser" to idUser,
            "complete" to complete,
            "catatan" to catatan,
            "createOn" to createOn,
            "kelas" to kelas,
            "name" to name,
            "address" to address,
            "urlPict" to urlPict,
            "nohp" to nohp,
            "nim" to nim,
            "email" to email,
            "timestamp" to timestamp
        )
    }
}