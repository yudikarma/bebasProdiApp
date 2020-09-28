package com.haekal.bebasprodiapp.ui.bebas_prodi.dosen.show


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment

import com.haekal.bebasprodiapp.R
import com.haekal.bebasprodiapp.data.BebasProdi
import com.haekal.bebasprodiapp.ui.base.BaseActivity
import com.haekal.bebasprodiapp.utils.getCallerActivity
import com.haekal.bebasprodiapp.utils.showAlert
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.haekal.bebasprodiapp.ui.picture.DetailPictureActivity
import com.haekal.bebasprodiapp.utils.onClick
import kotlinx.android.synthetic.main.fragment_show_parcel.*

/**
 * A simple [Fragment] subclass.
 */
class ShowBebasProdiActivity : BaseActivity() {
    companion object{
        const val ShowParcelRequestCode = 1033
        fun getStaredIntent(activityCaller:Any, data: BebasProdi, requestCode: Int,isCanEdit:Boolean){
            getCallerActivity(activityCaller)?.let {
                val intent = Intent(it.context, ShowBebasProdiActivity::class.java)
                intent.putExtra("data",data)
                intent.putExtra("isCanEdit",isCanEdit)
                it.startActivityForResult(intent,requestCode)
                //it.activity?.finish()

            }
        }
    }

    private lateinit var bebasProdi: BebasProdi
    private  var isCanEdit = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_show_parcel)

        bebasProdi = intent.getParcelableExtra("data")
        isCanEdit = intent.getBooleanExtra("isCanEdit",false)

        showLoadingDialog()
        mParcelDatabaseRefference.child(bebasProdi.id).addListenerForSingleValueEvent(object  : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                dismissLoadingDialog()
            }

            override fun onDataChange(it: DataSnapshot) {
               dismissLoadingDialog()

                bebasProdi = BebasProdi(
                    id = it.child("id").value.toString(),
                    freeKey= it.child("freeKey").value.toString(),
                    freeLab = it.child("freeLab").value.toString(),
                    freeLibrary = it.child("freeLibrary").value.toString(),
                    freKompen = it.child("freKompen").value.toString(),
                    buktiFreeKey = it.child("buktiFreeKey").value.toString(),
                    buktiFreeLab = it.child("buktiFreeLab").value.toString(),
                    buktiFreeLibrary = it.child("buktiFreeLibrary").value.toString(),
                    buktiFreeKompen = it.child("buktiFreeKompen").value.toString(),
                    remark = it.child("remark").value.toString(),
                    idUser = it.child("idUser").value.toString(),
                    complete = it.child("complete").value.toString(),
                    catatan = it.child("catatan").value.toString(),
                    createOn = it.child("createOn").value.toString(),
                    kelas = it.child("kelas").value.toString(),
                    name = it.child("name").value.toString(),
                    address = it.child("address").value.toString(),
                    urlPict = it.child("urlPict").value.toString(),
                    nohp = it.child("nohp").value.toString(),
                    nim = it.child("nim").value.toString(),
                    email = it.child("email").value.toString()
                )

                setupViewParcel(bebasProdi)


            }

        })

    }

    private fun setupViewParcel(data:BebasProdi?) {

        Log.d("data bebasProdi adalah","yow ${data?.toMap()}")


        data?.freeKey?.let {
               konfirm_free_loker.isChecked = it == "true"

        }

        data?.freeLibrary?.let {
            konfirm_free_perpus.isChecked = it == "true"

        }

        data?.freKompen?.let {
               konfirm_free_kompen.isChecked = it == "true"
        }

        data?.freeLab?.let {
            konfirm_free_lab.isChecked = it == "true"

        }

        btn_free_looker.onClick {
            data?.buktiFreeKey?.let {
                DetailPictureActivity.getStaredIntent(this,it)
            }
        }

        btn_free_kompen.onClick {
            data?.buktiFreeKompen?.let {
                DetailPictureActivity.getStaredIntent(this,it)
            }
        }

        btn_free_lab.onClick {
            data?.buktiFreeLab?.let {
                DetailPictureActivity.getStaredIntent(this,it)
            }
        }

        btn_free_perpus.onClick {
            data?.buktiFreeLibrary?.let {
                DetailPictureActivity.getStaredIntent(this,it)
            }
        }

        //setup disable enable
        if(data?.complete == "true" || isCanEdit == false){
            catatan.isEnabled = false
            konfirm_free_lab.isEnabled = false;
            konfirm_free_kompen.isEnabled = false;
            konfirm_free_loker.isEnabled = false;
            konfirm_free_perpus.isEnabled = false;
            btn_save.gone()

        }


        catatan.setText(data?.catatan)

        btn_save.onClick{

            data?.catatan = catatan.text.toString()
            data?.freeLab = if(konfirm_free_lab.isChecked) "true" else "false"
            data?.freKompen = if(konfirm_free_kompen.isChecked) "true" else "false"
            data?.freeKey = if(konfirm_free_loker.isChecked) "true" else "false"
            data?.freeLibrary = if(konfirm_free_perpus.isChecked) "true" else "false"

            data?.complete = ((data?.freeLab == "true" && data?.freKompen == "true"
                    && data?.freeLibrary == "true" && data?.freeKey == "true" ).toString())


            updateStatusParcel(data)

        }

       /* with(map_view) {
            // Initialise the MapView
            onCreate(null)
            getMapAsync {
                MapsInitializer.initialize(context)

                *//*data?.latitude?.let { lat ->
                    data?.longitude?.let { lng ->
                        setupMapView(LatLng(lat.toDouble(),lng.toDouble()),it)
                        this.visible()
                    }
                }*//*
            }
        }*/
    }

/*    fun setupMapView(destinationLatlng: LatLng, map: GoogleMap) {
        with(map) {
            moveCamera(CameraUpdateFactory.newLatLngZoom(destinationLatlng, 13f))
            addMarker(MarkerOptions().position(destinationLatlng))
            mapType = GoogleMap.MAP_TYPE_NORMAL
            setOnMapClickListener {
                //toast("click marker")
            }

        }
    }

    override fun onResume() {
        super.onResume()
        map_view.onResume()
    }

    override fun onPause() {
        super.onPause()
        map_view.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        map_view.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map_view.onLowMemory()
    }*/

    private fun updateStatusParcel(model:BebasProdi?) {
       // model?.idSupir = mUser?.uid.toString()
        //model?.status = AppConstans.PARCEL_IS_PICK_UP
        val childValue = model?.toMap()



        val childUpdates = HashMap<String, Map<String,Any?>?>()

        childUpdates["${model?.id}"] = childValue

        showLoadingDialog()
        mParcelDatabaseRefference.updateChildren(childUpdates as Map<String, Any>)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    dismissLoadingDialog()

                    //success save to database
                    dismissLoadingDialog()

                    showAlert(
                        "",
                        "Berhasil Mengupdate Data BebasProdi"
                    ) {
                        callToFinish()
                    }


                } else {
                    dismissLoadingDialog()
                    //fail register
                    showAlert("${task.exception?.message}", "")
                }
            }
    }

    fun callToFinish(){
        val intent = Intent()
        setResult(Activity.RESULT_OK,intent)
        finish()
    }


}
