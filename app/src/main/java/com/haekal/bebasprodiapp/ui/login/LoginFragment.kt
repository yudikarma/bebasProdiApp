package com.haekal.bebasprodiapp.ui.login


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController

import com.haekal.bebasprodiapp.R
import com.haekal.bebasprodiapp.ui.MainActivity
import com.haekal.bebasprodiapp.ui.base.BaseFragment
import com.haekal.bebasprodiapp.ui.forgot.ForgotPasswordFragment
import com.haekal.bebasprodiapp.ui.registrasi.RegistrasiFragment
import com.haekal.bebasprodiapp.ui.splash.SplashFragment
import com.haekal.bebasprodiapp.utils.onClick
import kotlinx.android.synthetic.main.fragment_login.*
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.toast

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : BaseFragment() {

    companion object{
        fun directionToMe(view: View){
           // val mainNavView = activity.findViewById<View>(R.id.main_fragment)
           // Navigation.findNavController(mainNavView).navigate(R.id.loginFragment)
            view?.let {
                Navigation.findNavController(it).navigate(R.id.loginFragment)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        forgotpassword.onClick {
            //direc to forgot password
            ForgotPasswordFragment.directionToMe(activity)
        }

        petugas.onClick {
                RegistrasiFragment.directionToMe(activity,"MAHASISWA") }
        supir.onClick { RegistrasiFragment.directionToMe(activity,"DOSEN") }

        email_sign_in_button.onClick { validateLogin() }
    }

    private fun validateLogin() {
        val valueEmail = email.text.toString()
        val valuePassword = password.text.toString()

        if (valueEmail.isEmpty() || valuePassword.isEmpty()){
            if (valueEmail.isEmpty())
                email.error = "Required"
            if (valuePassword.isEmpty())
                password.error = "Required"
        }else{
            showLoadingDialog()
            mAuth.signInWithEmailAndPassword(valueEmail,valuePassword).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    dismissLoadingDialog()
                    setLogout(false)

                    val intent = Intent(activity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    activity.finish()
                    /*activity?.let {
                        SplashFragment.directToMe(it)
                    }*/
                }else{
                    dismissLoadingDialog()
                    toast("gagal login ${task.exception?.message}")
                }
            }
        }
    }


}
