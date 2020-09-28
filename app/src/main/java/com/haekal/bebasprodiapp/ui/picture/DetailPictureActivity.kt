package com.haekal.bebasprodiapp.ui.picture

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.haekal.bebasprodiapp.R
import com.haekal.bebasprodiapp.utils.ekstension.loadFromUrl
import com.haekal.bebasprodiapp.utils.getCallerActivity
import kotlinx.android.synthetic.main.activity_detail_picture.*

class DetailPictureActivity : AppCompatActivity() {

    companion object{
        fun getStaredIntent( activityCaller:Any,urlImage:String){
            getCallerActivity(activityCaller)?.let {
                val intent = Intent(it.context, DetailPictureActivity::class.java)
                intent.putExtra("data",urlImage)
                it.startActivityForResult(intent,90)

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_picture)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Gambar"
        img.loadFromUrl(intent.getStringExtra("data"))
    }
}
