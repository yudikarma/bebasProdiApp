package com.haekal.bebasprodiapp.ui.base

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.haekal.bebasprodiapp.ui.dialog.LoadingDialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.haekal.bebasprodiapp.ui.MainActivity
import org.jetbrains.anko.alert
import org.jetbrains.anko.support.v4.startActivity

abstract class BaseActivity:AppCompatActivity(),LoadingDialogFragment.LoadingDialogFragmentListener {
    val loadingDialog: LoadingDialogFragment? by lazy { LoadingDialogFragment(this) }


    internal var mAuth : FirebaseAuth
    internal var mUser: FirebaseUser? = null
    internal var mDatabaseRefrence : DatabaseReference
    internal var mUserDatabaseReffrence : DatabaseReference
    internal var mParcelDatabaseRefference : DatabaseReference
    internal  var mFirbaseDatabse:FirebaseDatabase


    internal  var storage: FirebaseStorage
    internal  var imageProfilReference: StorageReference
    internal  var imageParcelReference: StorageReference





    init {
        mAuth = FirebaseAuth.getInstance()
        mUser = FirebaseAuth.getInstance().currentUser
        mFirbaseDatabse = FirebaseDatabase.getInstance()
        mDatabaseRefrence = mFirbaseDatabse.reference
        mUserDatabaseReffrence = mFirbaseDatabse.reference.child("Users")
        mParcelDatabaseRefference = mFirbaseDatabse.reference.child("BebasProdi")

        //init firebase storage
        storage = FirebaseStorage.getInstance()
        imageProfilReference = storage.reference.child("Users/")
        imageParcelReference = storage.reference.child("BebasProdi/")

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDismis(dialog: BottomSheetDialogFragment) {
        dismissLoadingDialog()
    }

    fun showLoadingDialog(){
        if (!(loadingDialog?.isAdded?:false)){
            loadingDialog?.isCancelable = false
            supportFragmentManager?.let {
                loadingDialog?.show(it,"loadingDialog")
            }
        }
    }

    fun showErrorDialog(errorMessage:String? = ""){
        baseContext.alert("$errorMessage","Terjadi Kesalahan")
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

    fun callToFinish_(finishWithResfresh:Boolean){
        if (finishWithResfresh){
            val intent = Intent()
            setResult(Activity.RESULT_OK,intent)
            finish()
        }else
            finish()
    }


    fun callToFinish_(){
        val intent = Intent()
        setResult(Activity.RESULT_OK,intent)
        finish()
    }

    fun logout() {
        val dialog : AlertDialog = AlertDialog.Builder(this)
            .setTitle("Keluar")
            .setMessage("Apakah kamu yakin ingin keluar ?")
            .setNeutralButton("CANCEL"){
                    dialog, which -> dialog.dismiss()
            }
            .setPositiveButton("OK"){
                    dialog, which ->

                if (mUser == null){
                    finish()
                }else {

                    FirebaseAuth.getInstance().signOut()

                    setLogout(true)
                    MainActivity.getStaredIntent(this,"true")
                    finish()
                }


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

    private fun setLogout(isLogout:Boolean) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = prefs.edit()
        editor.putBoolean("isLogout", isLogout)
        editor.commit()
    }

}