package com.haekal.bebasprodiapp.ui.dashboard.container_dosen


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI

import com.haekal.bebasprodiapp.R
import com.haekal.bebasprodiapp.ui.base.BaseActivity
import kotlinx.android.synthetic.main.fragment_contaienr_dahsboard_loket.*
import androidx.core.view.GravityCompat
import android.view.MenuItem
import android.widget.TextView
import com.haekal.bebasprodiapp.data.User
import com.haekal.bebasprodiapp.utils.ekstension.loadFromUrl
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView


/**
 * A simple [Fragment] subclass.
 */
class ContaienrDahsboardDosenActivity : BaseActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var display_profil_cirle:CircleImageView
    private lateinit var display_name:TextView
    private lateinit var display_job:TextView

    private var user:User? = User()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_contaienr_dahsboard_loket)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        toolbar.inflateMenu(R.menu.menu_main_loket)


        //init navcontroller
        navController = Navigation.findNavController(this, R.id.container)


        //init appbarconfiguration
        appBarConfiguration = AppBarConfiguration(navController.graph, drawer_layout_main)

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
