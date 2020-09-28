package com.haekal.bebasprodiapp.utils

import android.content.Context
import android.net.Uri
import androidx.fragment.app.FragmentManager
import com.haekal.bebasprodiapp.ui.dialog.LoadingDialogFragment
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.alert
import java.io.File
import java.util.*

object Firebase {
    fun uploadToFirebase(file: File, loadingDialog: LoadingDialogFragment?, imageReference:StorageReference, context: Context,fragmentManager: FragmentManager,onSucces:(imgurl:String) -> Unit ) {

       showLoadingDialog(loadingDialog,fragmentManager)

        val fileUri = Uri.fromFile(file)
        val metadata = StorageMetadata.Builder()
            .setContentType("image/jpeg")
            .build()
        val randomNameImage = UUID.randomUUID().toString()
        val uploadTask = imageReference.child("$randomNameImage").putFile(fileUri, metadata)
        uploadTask.addOnFailureListener { exception ->

            exception.message?.let { message -> context.alert(message) { positiveButton("OK") {} }.show() }
            dismissLoadingDialog(loadingDialog)

        }.addOnSuccessListener { succes ->
            imageReference.child("$randomNameImage").downloadUrl
                .addOnSuccessListener {
                    //success
                    dismissLoadingDialog(loadingDialog)

                    //return data
                    onSucces.invoke(it.toString())



                }.addOnFailureListener { exception ->
                    //failure
                    exception.message?.let { message -> context.alert(message) { positiveButton("OK") {} }.show() }
                   dismissLoadingDialog(loadingDialog)

                }
        }
    }

    private fun showLoadingDialog(loadingDialog: LoadingDialogFragment?,supportFragmentManager:FragmentManager){
        if (!(loadingDialog?.isAdded?:true)){
            loadingDialog?.isCancelable = false
            supportFragmentManager?.let {
                loadingDialog?.show(it,"loadingDialog")
            }
        }
    }

    fun dismissLoadingDialog(loadingDialog: LoadingDialogFragment?){
        loadingDialog?.dismiss()
    }


}