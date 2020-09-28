package com.haekal.bebasprodiapp.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import androidx.fragment.app.Fragment
import com.dekape.core.utils.toCalendar
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.alert
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit



object Utils {
    fun getCurrentDate(patern:String = "dd-MM-yyyy HH:mm"):String{
        var result = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern(patern)
            result = current.format(formatter)
        } else {
            val c = Calendar.getInstance().time
            val df = SimpleDateFormat(patern)
            result  = df.format(c)
        }
        return  result
    }

    fun getTimeStampFromDate(cal:Calendar):Double{
        return  cal.timeInMillis.toDouble()
    }


}

fun Context.showAlert(message: String) {
    val dialog = alert(message) { positiveButton("OK") {} }.build()
    dialog.setCancelable(false)
    dialog.show()
}

fun Context.showAlert(title: String?, message: String) {
    val dialog = alert(message, title) { positiveButton("OK") {} }.build()
    dialog.setCancelable(false)
    dialog.show()
}

fun Context.showAlert(
    title: String?,
    message: String,
    onClicked: (dialog: DialogInterface) -> Unit
) {
    val dialog = alert(message, title) { positiveButton("OK", onClicked) }.build()
    dialog.setCancelable(false)
    dialog.show()
}

fun Context.showConfirmation(
    title: String?,
    message: String,
    onClicked: (dialog: DialogInterface) -> Unit
) {
    val dialog = alert(message, title) {
        positiveButton("YES", onClicked)
        negativeButton("NO") {}
    }.build()

    dialog.setCancelable(false)
    dialog.show()
}

fun Activity.goToPermissionSetting(requestCode: Int, message: String) {
    val dialog = alert(message) {
        positiveButton("YES") {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivityForResult(intent, requestCode)
        }

        negativeButton("NO") { finish() }
    }.build()

    dialog.setCancelable(false)
    dialog.show()
}

/**
 * Activity Caller
 */
class ActivityCaller(
    val fragment: Fragment? = null,
    val activity: Activity? = null,
    val deprecatedFragment: android.app.Fragment? = null
) {
    val context: Context
        get() = (activity ?: fragment?.activity ?: deprecatedFragment?.activity)!!

    fun startActivityForResult(intent: Intent, chooser: Int) {
        activity?.startActivityForResult(intent, chooser)
            ?: fragment?.startActivityForResult(intent, chooser)
            ?: deprecatedFragment?.startActivityForResult(intent, chooser)
    }
    fun startActivity(intent: Intent) {
        activity?.startActivity(intent)
            ?: fragment?.startActivity(intent)
            ?: deprecatedFragment?.startActivity(intent)
    }
}

fun getCallerActivity(caller: Any): ActivityCaller? = when (caller) {
    is Activity -> ActivityCaller(activity = caller)
    is Fragment -> ActivityCaller(fragment = caller)
    is android.app.Fragment -> ActivityCaller(
        deprecatedFragment = caller
    )
    else -> null
}


fun View.onClick(onNext: (() -> Unit)) {
    return setOnClickListener {
        onNext.invoke()
    }
}

//convert a map to a data class
inline fun <reified T> Map<String, Any>.toDataClass(): T {
    return convert()
}

//convert a data class to a map
fun <T> T.serializeToMap(): Map<String, Any> {
    return convert()
}


//convert an object of type I to type O
inline fun <I, reified O> I.convert(): O {
    val gson = Gson()
    val json = gson.toJson(this)
    return gson.fromJson(json, object : TypeToken<O>() {}.type)
}

