package com.haekal.bebasprodiapp.ui.dashboard.container_mahasiswa


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.MenuItem
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController

import com.haekal.bebasprodiapp.R
import com.haekal.bebasprodiapp.data.User
import com.haekal.bebasprodiapp.ui.base.BaseActivity
import com.haekal.bebasprodiapp.utils.ekstension.loadFromUrl
import com.haekal.bebasprodiapp.utils.getCallerActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_container_dashboard.*

/**
 * A simple [Fragment] subclass.
 */
class ContainerDashboardMahasiswaActivity : BaseActivity() {

    companion object{
        fun getStaredIntent( activityCaller:Any){
            getCallerActivity(activityCaller)?.let {
                val intent = Intent(it.context, ContainerDashboardMahasiswaActivity::class.java)
                it.startActivity(intent)
                //it.activity?.finish()

            }
        }
    }
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var display_profil_cirle: CircleImageView
    private lateinit var display_name: TextView
    private lateinit var display_job:TextView

    private var user: User? = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_container_dashboard)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        toolbar.inflateMenu(R.menu.menu_main_supir)

        //init navcontroller
        navController = Navigation.findNavController(this, R.id.container)

        //init appbarconfiguration
        appBarConfiguration = AppBarConfiguration(navController.graph, drawer_layout_main)

        //setup nav controller to toolbar
        setupActionBarWithNavController(navController, appBarConfiguration)

        val logout :MenuItem = navigationMenu.menu.findItem(R.id.logout)
        logout.setOnMenuItemClickListener {
            logout()
            true
        }

        //setup nav controller to toolbar
        NavigationUI.setupActionBarWithNavController(this,navController, appBarConfiguration)

        //setup navigation controller
        NavigationUI.setupWithNavController(navigationMenu, navController)

        getUserInfo()

    }

    private fun getUserInfo() {
        mUserDatabaseReffrence.child(mUser?.uid?:"").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p: DatabaseError) {
                showErrorDialog("${p.message}")
            }

            override fun onDataChange(p: DataSnapshot) {
                user = p.getValue(User::class.java)
                Log.d("data snapshot","${p.value}")
                Log.d("data uuid","${mUser?.uid?:""}")
                Log.d("user info","$user")
                showUserInfo(user)
            }

        })
    }

    private fun showUserInfo(user:User? = User()) {
        //init
        val headerView = navigationMenu.getHeaderView(0)
        display_profil_cirle = headerView.findViewById(R.id.display_profil_cirle)
        display_name = headerView.findViewById(R.id.display_name)
        display_job = headerView.findViewById(R.id.display_job)

        //set data
        display_profil_cirle.loadFromUrl(user?.urlPict?:"")
        display_name.text = user?.name
        display_job.text = user?.rule
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,appBarConfiguration)
    }

    override fun onBackPressed() {
        if (drawer_layout_main.isDrawerOpen(GravityCompat.START)) {
            drawer_layout_main.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }



}
