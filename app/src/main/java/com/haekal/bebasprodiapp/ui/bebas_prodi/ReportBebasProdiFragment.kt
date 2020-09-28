package com.haekal.bebasprodiapp.ui.bebas_prodi


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.haekal.bebasprodiapp.R
import com.haekal.bebasprodiapp.ui.base.BaseFragment

/**
 * A simple [Fragment] subclass.
 */
class ReportBebasProdiFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_parcel, container, false)
    }


}
