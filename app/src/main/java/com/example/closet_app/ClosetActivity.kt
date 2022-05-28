package com.example.closet_app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.closet_app.databinding.ActivityClosetBinding
import com.example.closet_app.tracker.MultiBoxTracker
import com.example.graduateproject.classfiers.YoloClassfier
import com.example.graduateproject.classfiers.YoloInterfaceClassfier
import com.example.graduateproject.env.ImageUtils
import com.example.graduateproject.env.Utils
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class ClosetActivity : AppCompatActivity() {

    //lateinit var는 나중에 초기화를 하겠다는 선언입니다.
    //var : x? 로 정의하는 것도 좋지만, !!를 붙여야하는게 피곤해서 이렇게 바꿔봤습니다.
    lateinit var binding: ActivityClosetBinding
    lateinit  var recyclerView: RecyclerView


    var uri = ArrayList<Uri>()
    lateinit var adapter: RecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {

        binding=ActivityClosetBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //onCreate에 너무 많으니 정신없어서 initVariable로 분리했습니다.
        initVariable()

    }


    //lateinit var들을 초기화하고, 기능을 정의하는 함수입니다.
    //oncreate에 변수가 너무 많아, 분리했습니다.
    private fun initVariable() {

        val gohome=binding.gohome
        recyclerView=binding.recyclerView

        val pick=binding.pick
        adapter = RecyclerAdapter(uri)

        recyclerView.layoutManager=LinearLayoutManager(this@ClosetActivity,LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter


        pick.setOnClickListener {
            val myIntent=Intent(this,DetectActivity::class.java)
            startActivity(myIntent)
        }
        gohome.setOnClickListener {
            val myIntent = Intent(this, MainActivity::class.java)
            startActivity(myIntent)
        }


        if (ContextCompat.checkSelfPermission(
                this@ClosetActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@ClosetActivity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                Read_Permission
            )
        }
    }


    companion object {
        private const val Read_Permission = 101
    }
}