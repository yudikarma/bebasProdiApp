package com.haekal.bebasprodiapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.haekal.bebasprodiapp.R
import com.haekal.bebasprodiapp.ui.base.BaseActivity
import com.haekal.bebasprodiapp.utils.getCallerActivity

class MainActivity : BaseActivity() {
    companion object {
        const val MainActivityRequestCode = 10
        const val MainActivityEXTRA = "MainActivityEXTRA"
        fun getStaredIntent(activityCaller: Any, isLogout: String?=null) {
            getCallerActivity(activityCaller)?.let {
                val intent = Intent(it.context, MainActivity::class.java)
                intent.putExtra(MainActivityEXTRA, isLogout)
                it.startActivity(intent)
                //it.activity?.finish()

            }
        }
    }

    var isLogout :String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        isLogout = intent.getStringExtra(MainActivityEXTRA)
       setupFragmentMain()
    }

    private fun setupFragmentMain() {
        var fragment: Fragment? = null
        fragment = MainFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.commit()
    }


    override fun onStart() {
        super.onStart()

    }




}
