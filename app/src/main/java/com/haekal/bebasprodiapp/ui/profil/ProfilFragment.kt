package com.haekal.bebasprodiapp.ui.profil


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.haekal.bebasprodiapp.R
import com.haekal.bebasprodiapp.data.User
import com.haekal.bebasprodiapp.ui.base.BaseFragment
import com.haekal.bebasprodiapp.utils.ekstension.loadFromUrl
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_profil_supir.*

/**
 * A simple [Fragment] subclass.
 */
class ProfilFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profil_supir, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUserInfo()
    }

    private fun getUserInfo() {
        mUserDatabaseReffrence.child(mUser?.uid?:"").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p: DatabaseError) {
                showErrorDialog("${p.message}")
            }

            override fun onDataChange(p: DataSnapshot) {
                val user = p.getValue(User::class.java)


                showUserInfo(user)
            }

        })
    }

    private fun showUserInfo(user:User?){
        user?.let {
            tv_name.text = it.name
            tv_address.text = it.address
            email.text = it.email
            phone.text = it.nohp
            rule.text = it.rule

            profil_pict.loadFromUrl(it.urlPict)
        }
    }

}
