package com.haekal.bebasprodiapp.ui.bebas_prodi.mahasiswa.edit


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle

import com.haekal.bebasprodiapp.R
import com.haekal.bebasprodiapp.data.BebasProdi
import com.haekal.bebasprodiapp.ui.base.BaseActivity
import com.haekal.bebasprodiapp.ui.signature.CreateSignatureActivity
import com.haekal.bebasprodiapp.utils.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.haekal.bebasprodiapp.data.User
import com.haekal.bebasprodiapp.ui.picture.DetailPictureActivity
import com.haekal.bebasprodiapp.utils.ekstension.loadFromUrl
import kotlinx.android.synthetic.main.fragment_edit_parcel.*
import kotlinx.android.synthetic.main.fragment_edit_parcel.catatan
import kotlinx.android.synthetic.main.fragment_edit_parcel.is_free_kompen
import kotlinx.android.synthetic.main.fragment_edit_parcel.is_free_kunci_loker
import org.jetbrains.anko.toast
import permissions.dispatcher.*
import pl.aprilapps.easyphotopicker.*
import java.io.File
import kotlin.collections.HashMap


@RuntimePermissions
class CreateKonfirmasiBebasProdiActivity : BaseActivity(){
    companion object{
        const val CAMERA_PERMISSION_CODE = 909
        const val CreateKonfirmasiIntentCode = 987
        const val KUNCI_LOKER_CODE = 988
        const val LAB_CODE = 989
        const val PERPUS_CODE = 990
        const val KOMPEN_CODE = 991

        fun getStaredIntent(activityCaller:Any, requestCode: Int,data:BebasProdi?=null){
            getCallerActivity(activityCaller)?.let {
                val intent = Intent(it.context, CreateKonfirmasiBebasProdiActivity::class.java)
                intent.putExtra("data",data)
                it.startActivityForResult(intent,requestCode)

            }
        }

    }

    private var file_photo_kunci_loker: File? = null
    private var file_photo_kunci_loker_url :String = ""

    private var file_photo_lab: File? = null
    private var file_photo_lab_url :String = ""

    private var file_photo_perpus: File? = null
    private var file_photo_perpus_url :String = ""

    private var file_photo_kompensasi: File? = null
    private var file_photo_kompensasi_url :String = ""

    private var file_photo_signature_url :String = ""

    private  var bebasProdi: BebasProdi? = null

    private lateinit var user :User


    private val easyImage: EasyImage by lazy {
        EasyImage.Builder(this)
            .setChooserType(ChooserType.CAMERA_AND_GALLERY)
            .build()
    }
    private var lastRequestCode = CAMERA_PERMISSION_CODE


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_edit_parcel)

        bebasProdi = intent.getParcelableExtra("data") as? BebasProdi
        bebasProdi?.let {
            showUpdatebebasProdi(bebasProdi)
        }

        btn_update.onClick { validateData() }

        action_free_loker.onClick {pickFotoWithPermissionCheck(KUNCI_LOKER_CODE) }
        action_free_lab.onClick { pickFotoWithPermissionCheck(LAB_CODE) }
        action_free_kompen.onClick { pickFotoWithPermissionCheck(KOMPEN_CODE) }
        action_free_perpus.onClick { pickFotoWithPermissionCheck(PERPUS_CODE) }

        pick_signature.onClick { CreateSignatureActivity.getStaredIntent(this,CreateSignatureActivity.CreateSignatureRequestCode) }
    }

    private fun showUpdatebebasProdi(bebasProdi: BebasProdi?) {
        is_free_kompen.isChecked = bebasProdi?.freKompen == "true"
        is_free_kunci_loker.isChecked = bebasProdi?.freeKey == "true"
        is_free_perpus.isChecked = bebasProdi?.freeLibrary == "true"
        is_fre_lab.isChecked = bebasProdi?.freeLab == "true"

        img_loker.loadFromUrl(bebasProdi?.buktiFreeKey?:"")
        img_kompen.loadFromUrl(bebasProdi?.buktiFreeKompen?:"")
        img_lab.loadFromUrl(bebasProdi?.buktiFreeLab?:"")
        img_perpus.loadFromUrl(bebasProdi?.buktiFreeLibrary?:"")

        catatan.setText(bebasProdi?.catatan?:"")

        img_loker.visible()
        img_kompen.visible()
        img_lab.visible()
        img_perpus.visible()
    }

    private fun validateData() {
        val isFreeKunciLoker = is_free_kunci_loker.isChecked
        val isFreeKompen = is_free_kompen.isChecked
        val isFreeLab = is_fre_lab.isChecked
        val isFreePerpus = is_free_perpus.isChecked
        val mcatatan = catatan.text.toString()


        if (isFreeKunciLoker  && isFreeKompen && isFreeLab && isFreePerpus  && !mcatatan.isBlank()
            && !file_photo_kunci_loker_url.isBlank() && !file_photo_kompensasi_url.isBlank()
            && !file_photo_perpus_url.isBlank() && !file_photo_lab_url.isBlank()){

            showLoadingDialog()

            mUserDatabaseReffrence.child(mUser?.uid?:"").addListenerForSingleValueEvent(object  : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    showAlert(p0.message)
                    dismissLoadingDialog()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    p0.getValue(User::class.java)?.let {
                         user = it

                        val parcelRef = mParcelDatabaseRefference.push()
                        var id  = parcelRef.getKey()

                        mParcelDatabaseRefference.child(id?:"").addListenerForSingleValueEvent(object :ValueEventListener{
                            override fun onCancelled(p0: DatabaseError) {
                                showAlert(p0.message)
                                dismissLoadingDialog()
                            }

                            override fun onDataChange(p0: DataSnapshot) {

                                var dataBebasProdi = BebasProdi(
                                    id = id!!,
                                    idUser = mUser?.uid!!,
                                    freeKey = if(isFreeKunciLoker) "true" else "false",
                                    freKompen = if(isFreeKompen) "true" else "false",
                                    freeLab = if(isFreeLab) "true" else "false",
                                    freeLibrary = if(isFreeLab) "true" else "false",
                                    catatan = mcatatan,
                                    buktiFreeKey = file_photo_kunci_loker_url,
                                    buktiFreeKompen = file_photo_kompensasi_url,
                                    buktiFreeLab = file_photo_lab_url,
                                    buktiFreeLibrary = file_photo_perpus_url,
                                    createOn = Utils.getCurrentDate(),
                                    complete = "false",
                                    remark = mcatatan,
                                    kelas = user.kelas,
                                    urlPict = user.urlPict,
                                    nohp = user.nohp,
                                    nim = user.nim,
                                    name = user.name,
                                    address = user.address,
                                    email = user.email,
                                    timestamp = ServerValue.TIMESTAMP
                                )


                                val childValue = dataBebasProdi!!.toMap()



                                val childUpdates = HashMap<String, Any>()

                                childUpdates["${dataBebasProdi?.id}"] = childValue
                                mParcelDatabaseRefference.updateChildren(childUpdates)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            //success save to database
                                            dismissLoadingDialog()

                                            showAlert(
                                                "Berhasil Menyimpan Data BebasProdi",
                                                ""
                                            ) {
                                                callToFinish_(true)
                                            }


                                        } else {
                                            //fail register
                                            showAlert("${task.exception?.message}", "") {
                                                dismissLoadingDialog()
                                            }
                                        }
                                    }
                            }

                        })


                    }

                }

            })

        }else{
            toast("Harap Masukkan Semua Data");
        }
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
        goToPermissionSetting(CAMERA_PERMISSION_CODE,getString(R.string.msg_permission_camera))
    }

    @OnPermissionDenied(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    fun onPermissionCameraDenied() {
        showAlert("Permission Help",getString(R.string.msg_permission_camera)){
            pickFoto(lastRequestCode)
        }
    }
    @OnNeverAskAgain(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    fun onPermissionCameraNeverAskAgain() {
        showAlert("Permission Help",getString(R.string.msg_permission_camera)){
            goToPermissionSetting(lastRequestCode,getString(R.string.msg_permission_camera))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK){
            when(requestCode){
                CreateSignatureActivity.CreateSignatureRequestCode -> {
                    file_photo_signature_url = data?.getStringExtra(CreateSignatureActivity.CreateSignatureExtra)?:""
                    pick_signature.text = file_photo_signature_url
                }
            }
        }
        easyImage.handleActivityResult(requestCode,resultCode,data,this,object : DefaultCallback(){

            override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                val file_name = imageFiles[0].file
                file_name?.let {
                    when(lastRequestCode){

                        KUNCI_LOKER_CODE -> {
                            file_photo_kunci_loker = file_name
                            file_photo_kunci_loker?.let {
                                Firebase.uploadToFirebase(it,loadingDialog,imageProfilReference,this@CreateKonfirmasiBebasProdiActivity,supportFragmentManager){ successUri->
                                    file_photo_kunci_loker_url = successUri
                                    img_loker.loadFromUrl(file_photo_kunci_loker_url)
                                    img_loker.visible()
                                    img_loker.onClick { DetailPictureActivity.getStaredIntent(this,file_photo_kunci_loker_url) }
                                }
                            }
                        }

                        KOMPEN_CODE -> {
                            file_photo_kompensasi = file_name
                            file_photo_kompensasi?.let {
                                Firebase.uploadToFirebase(it,loadingDialog,imageProfilReference,this@CreateKonfirmasiBebasProdiActivity,supportFragmentManager){ successUri->
                                    file_photo_kompensasi_url = successUri
                                    img_kompen.loadFromUrl(file_photo_kompensasi_url)
                                    img_kompen.visible()
                                    img_kompen.onClick { DetailPictureActivity.getStaredIntent(this,file_photo_kompensasi_url) }

                                }
                            }
                        }

                        LAB_CODE -> {
                            file_photo_lab = file_name
                            file_photo_lab?.let {
                                Firebase.uploadToFirebase(it,loadingDialog,imageProfilReference,this@CreateKonfirmasiBebasProdiActivity,supportFragmentManager){ successUri->
                                    file_photo_lab_url = successUri
                                    img_lab.loadFromUrl(file_photo_lab_url)
                                    img_lab.visible()
                                    img_lab.onClick { DetailPictureActivity.getStaredIntent(this,file_photo_lab_url) }

                                }
                            }
                        }

                        PERPUS_CODE -> {
                            file_photo_perpus = file_name
                            file_photo_perpus?.let {
                                Firebase.uploadToFirebase(it,loadingDialog,imageProfilReference,this@CreateKonfirmasiBebasProdiActivity,supportFragmentManager){ successUri->
                                    file_photo_perpus_url = successUri
                                    img_perpus.loadFromUrl(file_photo_perpus_url)
                                    img_perpus.visible()
                                    img_perpus.onClick { DetailPictureActivity.getStaredIntent(this,file_photo_perpus_url) }

                                }
                            }
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

}
