package com.haekal.bebasprodiapp.ui.registrasi


import android.Manifest
import android.content.Intent
import android.net.Uri
import com.haekal.bebasprodiapp.ui.base.BaseFragment
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth

import com.haekal.bebasprodiapp.R
import com.haekal.bebasprodiapp.data.User
import com.haekal.bebasprodiapp.utils.AppConstans
import com.haekal.bebasprodiapp.utils.ekstension.loadFromUrl
import com.haekal.bebasprodiapp.utils.goToPermissionSetting
import com.haekal.bebasprodiapp.utils.showAlert
import com.google.firebase.storage.StorageMetadata
import com.haekal.bebasprodiapp.ui.login.LoginFragment
import com.haekal.bebasprodiapp.utils.onClick
import kotlinx.android.synthetic.main.fragment_registrasi.*
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.selector
import org.jetbrains.anko.support.v4.toast
import permissions.dispatcher.*
import pl.aprilapps.easyphotopicker.*
import java.io.File
import java.util.*
import kotlin.collections.HashMap
import android.widget.Toast
import android.view.KeyEvent.KEYCODE_BACK



/**
 * A simple [Fragment] subclass.
 */
@RuntimePermissions
class RegistrasiFragment : BaseFragment() {

    companion object {

        const val CAMERA_PERMISSION_CODE = 909
        fun directionToMe(activity: FragmentActivity, rule: String) {
            val mainNavView = activity.findViewById<View>(R.id.main_fragment)
            var bundle = bundleOf("rule" to rule)
            Navigation.findNavController(mainNavView).navigate(R.id.registrasiFragment, bundle)
        }
    }


    private var rule = AppConstans.RULE.MAHASISWA



    private var file_photo_profil: File? = null
    private var file_photo_profil_url :String = ""

    val listKelas = listOf("PILIH KELAS","1A", "1B", "2A", "2B","3A","3B")

    var selecteedKelas = listKelas[0]

    private val easyImage: EasyImage by lazy {
        EasyImage.Builder(context)
            .setChooserType(ChooserType.CAMERA_AND_GALLERY)
            .build()
    }

    private var lastRequestCode = CAMERA_PERMISSION_CODE

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registrasi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val safeArgs: RegistrasiFragmentArgs by navArgs()
         when (safeArgs.rule) {
            "MAHASISWA" -> {
                rule = AppConstans.RULE.MAHASISWA
                reg_kelas.visible()
            }
            else -> {
                rule = AppConstans.RULE.DOSEN
                reg_kelas.invisible()
            }
        }

        reg_kelas.setText(selecteedKelas)
        reg_kelas.onClick {
                selector("Pilih Kelas",listKelas) { dialogInterface, i ->
                    selecteedKelas = listKelas[i]
                    reg_kelas.setText(selecteedKelas)
                }
        }

        regist_btn.onClick { validateDataregister() }

        btn_camera.onClick {
            pickFotoWithPermissionCheck(lastRequestCode)
         }

        getView()!!.isFocusableInTouchMode = true
        getView()!!.requestFocus()

        getView()!!.setOnKeyListener(object : View.OnKeyListener {
            override
            fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                if (event.getAction() === KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        LoginFragment.directionToMe(view)
                        return true
                    }
                }
                return false
            }
        })

    }


    @NeedsPermission(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    fun pickFoto(requestCode:Int) {
        this.lastRequestCode = requestCode
        easyImage.openChooser(this)

    }

    @OnShowRationale(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    fun onShowRationaleDialogCamera(request: PermissionRequest) {
       activity.goToPermissionSetting(CAMERA_PERMISSION_CODE,getString(R.string.msg_permission_camera))
    }

    @OnPermissionDenied(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    fun onPermissionCameraDenied() {
       context.showAlert("Permission Help",getString(R.string.msg_permission_camera)){
           pickFoto(lastRequestCode)
       }
    }

    @OnNeverAskAgain(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    fun onPermissionCameraNeverAskAgain() {
        context.showAlert("Permission Help",getString(R.string.msg_permission_camera)){
            activity.goToPermissionSetting(lastRequestCode,getString(R.string.msg_permission_camera))
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        easyImage.handleActivityResult(requestCode,resultCode,data,activity,object : DefaultCallback(){
            override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                val file_name = imageFiles[0].file
                file_name?.let {
                    when(lastRequestCode){

                        CAMERA_PERMISSION_CODE -> {
                            file_photo_profil = file_name
                            file_photo_profil?.let { uploadToFirebase(it) }
                        }
                        else ->{
                            //do nothing
                        }
                    }
                }


            }

            override fun onImagePickerError(error: Throwable, source: MediaSource) {
               showErrorDialog("${error.message}")
            }


        })
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    private fun validateDataregister() {
        val email = reg_email?.text.toString()
        val password = reg_password?.text.toString()
        val name = reg_display_name?.text.toString()
        val address = reg_alamat?.text.toString()
        val noHp = reg_no_hp.text.toString()
        val nim = reg_nim.text.toString()


        if (email.isEmpty() || password.isEmpty() ||
            name.isEmpty() || address.isEmpty() || nim.isEmpty() ||
            noHp.isEmpty() || (rule == AppConstans.RULE.MAHASISWA && selecteedKelas == listKelas[0])
        ) {

            if (email.isEmpty()) {
                reg_email.error = "Required"
                return
            }

            if (password.isEmpty()) {
                reg_password.error = "Required"
                return
            }

            if (name.isEmpty()) {
                reg_display_name.error = "Required"
                return
            }

            if (address.isEmpty()) {
                reg_alamat.error = "Required"
                return
            }

            if (noHp.isEmpty()) {
                reg_alamat.error = "Required"
                return
            }

            if (nim.isEmpty()) {
                reg_alamat.error = "Required"
                return
            }

            if(rule != AppConstans.RULE.DOSEN)
                if(selecteedKelas == listKelas[0])
                toast("Harap Pilih Kelas")


        } else {

            if(password.length <= 7){
                toast("Password harus lebih dari 8 karakter")
            }else{
                //call function to register
                registerUser()
            }
        }
    }

    private fun registerUser() {

        val email = reg_email?.text.toString()
        val password = reg_password?.text.toString()
        val name = reg_display_name?.text.toString()
        val address = reg_alamat?.text.toString()
        val noHp = reg_no_hp.text.toString()
        val nim = reg_nim.text.toString()

        val profilPict = file_photo_profil_url
        val mRule = when (rule) {
            AppConstans.RULE.MAHASISWA -> "MAHASISWA"
            else -> "DOSEN"
        }



        showLoadingDialog()
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { taskAuth ->
                if (taskAuth.isSuccessful) {

                    var uid = mAuth.uid.toString()
                    val user = User(uid = uid,email = email,address = address,kelas = selecteedKelas,name = name,nim = nim,nohp = noHp,password = password,rule = mRule,urlPict = profilPict)


                    //save data user to database
                    val childValue = user.toMap()

                    val childUpdates = HashMap<String, Any>()
                    childUpdates["${mAuth.uid}"] = childValue
                    mUserDatabaseReffrence.updateChildren(childUpdates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                //success save to database
                                dismissLoadingDialog()

                                context.showAlert(
                                    "Registrasi Berhasil",
                                    "Selamat datang di sistem kami."
                                ){
                                    view?.let {
                                        LoginFragment.directionToMe(it)
                                    }
                                }


                            } else {
                                //fail register
                                context.showAlert("${task.exception?.message}", "") {
                                    dismissLoadingDialog()
                                }
                            }
                        }

                } else {
                    //fail create user to firebase auth
                    //fail create user to firebase auth
                    if (taskAuth.exception?.message.equals("The email address is already in use by another account.")){
                        mAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val user = FirebaseAuth.getInstance().currentUser
                                    user?.delete()
                                        ?.addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                registerUser()
                                            }
                                        }

                                }
                            }
                    }else{
                        showErrorDialog(taskAuth.exception?.message)
                        dismissLoadingDialog()
                    }
                }
            }
    }

    private fun uploadToFirebase(file: File) {

        showLoadingDialog()

        val fileUri = Uri.fromFile(file)
        val metadata = StorageMetadata.Builder()
            .setContentType("image/jpeg")
            .build()
        val randomNameImage = UUID.randomUUID().toString()
        val uploadTask = imageProfilReference.child("$randomNameImage").putFile(fileUri, metadata)
        uploadTask.addOnFailureListener { exception ->

            exception.message?.let { message -> alert(message) { positiveButton("OK") {} }.show() }
            dismissLoadingDialog()

        }.addOnSuccessListener { succes ->
            imageProfilReference.child("$randomNameImage").downloadUrl
                .addOnSuccessListener {
                    //success
                    dismissLoadingDialog()
                    file_photo_profil_url = it.toString()

                    //show image
                    avatar.loadFromUrl(it.toString())

                }.addOnFailureListener { exception ->
                    //failure
                    exception.message?.let { message -> alert(message) { positiveButton("OK") {} }.show() }
                    dismissLoadingDialog()

                }
        }
    }


}
