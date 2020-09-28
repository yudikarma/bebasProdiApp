package com.haekal.bebasprodiapp.ui.splash


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.haekal.bebasprodiapp.R
import com.haekal.bebasprodiapp.ui.base.BaseFragment
import com.haekal.bebasprodiapp.ui.dashboard.container_dosen.ContaienrDahsboardDosenActivity
import com.haekal.bebasprodiapp.ui.dashboard.container_mahasiswa.ContainerDashboardMahasiswaActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import com.haekal.bebasprodiapp.ui.login.LoginFragment
import org.jetbrains.anko.startActivity


/**
 * A simple [Fragment] subclass.
 */
class SplashFragment : BaseFragment() {

    companion object{
        private const val SPLASH_DELAY = 100L

        fun directToMe(fragmentActivity: FragmentActivity){
            val mainNavView = fragmentActivity.findViewById<View>(R.id.main_fragment)
            Navigation.findNavController(mainNavView).navigate(R.id.spalshFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spalsh, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler().postDelayed({
            mUser = FirebaseAuth.getInstance().currentUser
            if (mUser == null){
                //not yet login
                activity?.let { LoginFragment.directionToMe(view) }
            }else{
                //user login
                getUserRole()
            }
        }, SPLASH_DELAY)

    }

    private fun getUserRole() {
        mUserDatabaseReffrence.child(mUser?.uid?:"").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                showErrorDialog("${p0.message}")
            }

            override fun onDataChange(p0: DataSnapshot) {
                val rule = p0.child("rule").value
                when(rule){
                    "DOSEN" -> {
                        /*context?.startActivity<ContaienrDahsboardDosenActivity>()
                        activity?.finish()*/
                        if (!isLogout()) {
                            val intent =
                                Intent(activity, ContaienrDahsboardDosenActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            activity.startActivity(intent)
                            activity.finish()
                        }else{
                            activity?.let { view?.let { LoginFragment.directionToMe(it) } }
                        }

                    }
                    else -> {
                        context?.startActivity<ContainerDashboardMahasiswaActivity>()
                        activity?.finish()

                    }
                }
            }

        })
    }

    private fun isLogout():Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getBoolean("isLogout", false)

    }


}
