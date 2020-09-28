package com.haekal.bebasprodiapp.ui.forgot


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation

import com.haekal.bebasprodiapp.R

/**
 * A simple [Fragment] subclass.
 */
class ForgotPasswordFragment : Fragment() {

    companion object{
        fun directionToMe(activity: FragmentActivity){
            val mainNavView = activity.findViewById<View>(R.id.main_fragment)
            Navigation.findNavController(mainNavView).navigate(R.id.forgotPasswordFragment)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }


}
