package com.haekal.bebasprodiapp.ui.dashboard.mahasiswa


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import com.dekape.core.picker.OldDatePickerDialog

import com.haekal.bebasprodiapp.R
import com.haekal.bebasprodiapp.data.BebasProdi
import com.haekal.bebasprodiapp.ui.base.BaseFragment
import com.haekal.bebasprodiapp.ui.bebas_prodi.dosen.show.ShowBebasProdiActivity
import com.haekal.bebasprodiapp.ui.bebas_prodi.mahasiswa.edit.CreateKonfirmasiBebasProdiActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.haekal.bebasprodiapp.ui.dashboard.dosen.DashboardDosenFragment.Companion.DATE_CODE_FROM
import com.haekal.bebasprodiapp.ui.dashboard.dosen.DashboardDosenFragment.Companion.DATE_CODE_TO
import com.haekal.bebasprodiapp.utils.Utils
import com.haekal.bebasprodiapp.utils.onClick
import kotlinx.android.synthetic.main.fragment_dashboard_supir.*
import kotlinx.android.synthetic.main.view_calendar_between.*
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class DashboardMahasiswaFragment : BaseFragment(), DashboardMahasiswaFragmentRvAdapters.Interaction,
    OldDatePickerDialog.OnDateSetListener{


    companion object{
        fun directionToMe(activity: FragmentActivity){
            val mainNavView = activity.findViewById<View>(R.id.main_fragment)
            Navigation.findNavController(mainNavView).navigate(R.id.supirFragment)
        }
    }


    private var rvAdapters =  DashboardMahasiswaFragmentRvAdapters(this)

    private var selectedDateFrom = Calendar.getInstance()
    private var selectedDateFromString = ""
    private var selectedDateTo = Calendar.getInstance()
    private var selectedDateToString = ""
    private var minimumDate = Calendar.getInstance().apply { set(2014, 1, 31) }
    private var maximumDate = Calendar.getInstance().apply { set(2030, 11, 30) }


    private var lasDateCode = DATE_CODE_FROM;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard_supir, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        click_date_from.onClick {
            openDatePicker(DATE_CODE_FROM)
        }

        click_date_to.onClick {
            openDatePicker(DATE_CODE_TO)

        }
        selectedDateFrom.set(2014,6,1,1,1,1)
        //selectedDateTo.set(2030,11,28,23,59,59)
        val mFrom = (selectedDateFrom.get(Calendar.MONTH) + 1).toString().padStart(2, '0')
        val dFrom = (selectedDateFrom.get(Calendar.DAY_OF_MONTH)).toString().padStart(2, '0')

        val mTo = (selectedDateTo.get(Calendar.MONTH) + 1).toString().padStart(2, '0')
        val dTo = (selectedDateTo.get(Calendar.DAY_OF_MONTH)).toString().padStart(2, '0')
        selectedDateFromString = String.format("%s/%s/%d", dFrom,mFrom,selectedDateFrom.get(Calendar.YEAR))
        selectedDateToString = String.format("%s/%s/%d", dTo,mTo,selectedDateTo.get(Calendar.YEAR))
        txt_date_from?.text = selectedDateFromString
        txt_date_until?.text = selectedDateToString

        getListParcel()

        crete_parcel.onClick {
            CreateKonfirmasiBebasProdiActivity.getStaredIntent(this,CreateKonfirmasiBebasProdiActivity.CreateKonfirmasiIntentCode)
        }


    }

    private fun openDatePicker(lastDateCode:Int){
        this.lasDateCode = lastDateCode
        fragmentManager?.let {
            OldDatePickerDialog
                .setTitle("Pilih Tanggal")
                .setButtonText("Pilih")
                .setMaxDate(maximumDate)
                .setMinDate(minimumDate)
                .setSelectedDate(if(lasDateCode == DATE_CODE_FROM) selectedDateFrom else selectedDateTo)
                .build(this)
                .show(it, "picker")
        }
    }

    private fun getListParcel() {
        val listBebasProdi :ArrayList<BebasProdi?> = ArrayList()
        showLoadingDialog()
        mParcelDatabaseRefference.orderByChild("timestamp")
            .startAt(Utils.getTimeStampFromDate(selectedDateFrom)).endAt(Utils.getTimeStampFromDate(selectedDateTo)).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onCancelled(p: DatabaseError) {
                dismissLoadingDialog()
                showErrorDialog("${p.message}")
            }

            override fun onDataChange(p: DataSnapshot) {
                val dataSnapshot = p.children

                dataSnapshot?.forEach {
                    val bebasProdi = BebasProdi(
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

                    if(bebasProdi.idUser == mUser?.uid)
                        listBebasProdi.add(bebasProdi)

                }
                listBebasProdi?.let { it1 -> rvAdapters.submitList(it1) }
                rv_parcel.adapter = rvAdapters

                dismissLoadingDialog()



            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK){
            when(requestCode){
                CreateKonfirmasiBebasProdiActivity.CreateKonfirmasiIntentCode -> {
                    getListParcel()
                }
            }
        }
    }



    override fun onItemSelected(position: Int, item: BebasProdi) {
       /* when(item.status){
            AppConstans.PARCEL_IS_CREATED -> {
                ShowBebasProdiActivity.getStaredIntent(this,item,ShowBebasProdiActivity.ShowParcelRequestCode,true)
            }
            AppConstans.PARCEL_IS_PICK_UP -> {
                CreateKonfirmasiBebasProdiActivity.getStaredIntent(this,item,CreateKonfirmasiBebasProdiActivity.CreateKonfirmasiIntentCode)
            }
            else -> {
                //selesai
                ShowBebasProdiActivity.getStaredIntent(this,item,ShowBebasProdiActivity.ShowParcelRequestCode,false)

            }

        }*/
        //CreateKonfirmasiBebasProdiActivity.getStaredIntent(this,item,CreateKonfirmasiBebasProdiActivity.CreateKonfirmasiIntentCode)
        if(item.complete == "false")
            CreateKonfirmasiBebasProdiActivity.getStaredIntent(this,CreateKonfirmasiBebasProdiActivity.CreateKonfirmasiIntentCode,item)
        else
            ShowBebasProdiActivity.getStaredIntent(this,item,ShowBebasProdiActivity.ShowParcelRequestCode,false)

    }

    override fun onDateSet(year: Int, month: Int, dayOfMonth: Int) {

        if(lasDateCode == DATE_CODE_FROM){
            selectedDateFrom.set(year, month, dayOfMonth,1,1,1)
            val mFrom = (selectedDateFrom.get(Calendar.MONTH) + 1).toString().padStart(2, '0')
            val dFrom = (selectedDateFrom.get(Calendar.DAY_OF_MONTH)).toString().padStart(2, '0')
            selectedDateFromString = String.format("%s/%s/%d", dFrom,mFrom,selectedDateFrom.get(Calendar.YEAR))

            txt_date_from?.text = selectedDateFromString

        } else{
            selectedDateTo.set(year, month, dayOfMonth,23,59,59)
            val mTo = (selectedDateTo.get(Calendar.MONTH) + 1).toString().padStart(2, '0')
            val dTo = (selectedDateTo.get(Calendar.DAY_OF_MONTH)).toString().padStart(2, '0')
            selectedDateToString = String.format("%s/%s/%d", dTo,mTo,selectedDateTo.get(Calendar.YEAR))
            txt_date_until?.text = selectedDateToString


        }
        getListParcel()

    }


}
