package com.haekal.bebasprodiapp.ui.base

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.haekal.bebasprodiapp.ui.dialog.LoadingDialogFragment
import com.haekal.bebasprodiapp.utils.showAlert
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.haekal.bebasprodiapp.ui.MainActivity
import org.jetbrains.anko.support.v4.startActivity

abstract class BaseFragment:Fragment(), LoadingDialogFragment.LoadingDialogFragmentListener {

    private val loadingDialog: LoadingDialogFragment? by lazy { LoadingDialogFragment(this) }

    internal lateinit var context: Context
    internal lateinit var activity : FragmentActivity

    internal var mAuth : FirebaseAuth
    internal var mUser: FirebaseUser? = null
    internal var mDatabaseRefrence : DatabaseReference
    internal var mUserDatabaseReffrence :DatabaseReference
    internal var mParcelDatabaseRefference : DatabaseReference

    internal  var storage: FirebaseStorage
    internal  var imageProfilReference: StorageReference
    internal  var imageParcelReference: StorageReference

    init {
        mAuth = FirebaseAuth.getInstance()
        mUser = FirebaseAuth.getInstance().currentUser
        mDatabaseRefrence = FirebaseDatabase.getInstance().reference
        mUserDatabaseReffrence = FirebaseDatabase.getInstance().reference.child("Users")
        mParcelDatabaseRefference = FirebaseDatabase.getInstance().reference.child("BebasProdi")

        //init firebase storage
        storage = FirebaseStorage.getInstance()
        imageProfilReference = storage.reference.child("Users/")
        imageParcelReference = storage.reference.child("BebasProdi/")

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = getContext() ?: return
        activity = getActivity() ?: return
    }

    override fun onDismis(dialog: BottomSheetDialogFragment) {
        dismissLoadingDialog()
    }

    fun showLoadingDialog(){
        if (!(loadingDialog?.isAdded?:true)){
            loadingDialog?.isCancelable = false
            fragmentManager?.let {
                loadingDialog?.show(it,"loadingDialog")
            }
        }
    }

    fun showErrorDialog(errorMessage:String? = ""){
        context.showAlert("$errorMessage")
    }

    fun dismissLoadingDialog(){
        if (loadingDialog?.isAdded?:false)
            loadingDialog?.dismiss()
    }

    fun View.isVisible(): Boolean = visibility == View.VISIBLE

    fun View.visible() {
        visibility = View.VISIBLE
    }

    fun View.gone() {
        visibility = View.GONE
    }

    fun View.invisible() {
        visibility = View.INVISIBLE
    }

    fun logout() {
        val dialog : AlertDialog = AlertDialog.Builder(context)
            .setTitle("Keluar")
            .setMessage("Apakah kamu yakin ingin keluar ?")
            .setNeutralButton("CANCEL"){
                    dialog, which -> dialog.dismiss()
            }
            .setPositiveButton("OK"){
                    dialog, which ->



                FirebaseAuth.getInstance().signOut()
                startActivity<MainActivity>()
                activity.finish()
                true
            }

            .create()
        dialog.setOnShowListener(object : DialogInterface.OnShowListener{
            override fun onShow(arg0: DialogInterface?) {
                dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#BDBDBD"))
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#00bcd4"))
            } }
        )
        dialog.show()

    }

    fun setLogout(isLogout:Boolean) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putBoolean("isLogout", isLogout)
        editor.commit()
    }

}