package com.haekal.bebasprodiapp.ui.signature

import android.Manifest
import android.os.Bundle
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.widget.Toast
import com.github.gcacace.signaturepad.views.SignaturePad
import android.net.Uri
import android.os.Environment
import com.haekal.bebasprodiapp.R
import com.haekal.bebasprodiapp.ui.base.BaseActivity
import com.haekal.bebasprodiapp.utils.Firebase
import com.haekal.bebasprodiapp.utils.getCallerActivity
import com.haekal.bebasprodiapp.utils.onClick
import kotlinx.android.synthetic.main.activity_create_signature.*
import org.jetbrains.anko.toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter


class CreateSignatureActivity : BaseActivity() {

    companion object{
        const val CreateSignatureExtra = "imgTtd"
        const val CreateSignatureRequestCode = 43
        fun getStaredIntent( activityCaller:Any,requestCode: Int){
            getCallerActivity(activityCaller)?.let {
                val intent = Intent(it.context, CreateSignatureActivity::class.java)
                it.startActivityForResult(intent,requestCode)
                //it.activity?.finish()

            }
        }
    }
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private var file_photo_ttd_url :String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verifyStoragePermissions(this)
        setContentView(R.layout.activity_create_signature)

        signature_pad.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {

            }

            override fun onSigned() {
                save_button.setEnabled(true)
                clear_button.setEnabled(true)
            }

            override fun onClear() {
                save_button.setEnabled(false)
                clear_button.setEnabled(false)
            }
        })


        clear_button.onClick {  signature_pad.clear() }

        save_button.onClick {
            val signatureBitmap = signature_pad.signatureBitmap

            addJpgSignatureToGallery(signatureBitmap)

            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "Cannot write images to external storage",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun getAlbumStorageDir(albumName: String): File {
        // Get the directory for the user's public pictures directory.
        val file = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            ), albumName
        )
        if (!file.mkdirs()) {

        }
        return file
    }

    @Throws(IOException::class)
    fun saveBitmapToJPG(bitmap: Bitmap, photo: File) {
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        val stream = FileOutputStream(photo)
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        stream.close()
    }

    fun addJpgSignatureToGallery(signature: Bitmap) {
        try {
            val photo = File(
                getAlbumStorageDir("SignaturePad"),
                String.format("Signature_%d.jpg", System.currentTimeMillis())
            )
            saveBitmapToJPG(signature, photo)

            scanMediaFile(photo)

            //upload to firebase
            uploadToFirebase(photo)

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun scanMediaFile(photo: File) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val contentUri = Uri.fromFile(photo)
        mediaScanIntent.data = contentUri
        this.sendBroadcast(mediaScanIntent)
    }

    fun addSvgSignatureToGallery(signatureSvg: String): Boolean {
        var result = false
        try {
            val svgFile = File(
                getAlbumStorageDir("SignaturePad"),
                String.format("Signature_%d.svg", System.currentTimeMillis())
            )
            val stream = FileOutputStream(svgFile)
            val writer = OutputStreamWriter(stream)
            writer.write(signatureSvg)
            writer.close()
            stream.flush()
            stream.close()
            scanMediaFile(svgFile)
            result = true
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return result
    }

    /**
     * Checks if the app has permission to write to device storage
     *
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity the activity from which permissions are checked
     */
    fun verifyStoragePermissions(activity: Activity) {
        // Check if we have write permission
        val permission =
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }

    private fun uploadToFirebase(file: File) {

        toast("${file.path}")
        Firebase.uploadToFirebase(file,loadingDialog,imageProfilReference,this@CreateSignatureActivity,supportFragmentManager){ successUri->
            file_photo_ttd_url = successUri

            callToFinish()
        }

    }

    fun callToFinish(){
        val intent = Intent()
        intent.putExtra(CreateSignatureExtra,file_photo_ttd_url)
       toast("$file_photo_ttd_url")
        setResult(Activity.RESULT_OK,intent)
        finish()
    }
}
