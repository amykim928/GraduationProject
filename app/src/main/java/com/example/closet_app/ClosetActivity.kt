package com.example.closet_app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.closet_app.adapter.RecyclerAdapter
import com.example.closet_app.databinding.ActivityClosetBinding
import java.io.File
import kotlin.collections.ArrayList

class ClosetActivity : AppCompatActivity() {

    //lateinit var는 나중에 초기화를 하겠다는 선언입니다.
    //var : x? 로 정의하는 것도 좋지만, !!를 붙여야하는게 피곤해서 이렇게 바꿔봤습니다.
    lateinit var binding: ActivityClosetBinding
    lateinit  var recyclerView: RecyclerView

    //detectActivity에서 ArrayList<Uri>를 건네주는 식으로 바꾸려고 하네요.
    val bit = ArrayList<Bitmap>()
    lateinit var adapter: RecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {

        binding=ActivityClosetBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //onCreate에 너무 많으니 정신없어서 initVariable로 분리했습니다.
        setBits()
        initVariable()

    }

    private fun setBits() {
        val file = File(filesDir.toString())
        val files = file.listFiles()

        for (tempFile in files!!) {
            val path=filesDir.toString()+"/"+tempFile.name
            bit.add(BitmapFactory.decodeFile(path))
        }

    }


    //lateinit var들을 초기화하고, 기능을 정의하는 함수입니다.
    //oncreate에 변수가 너무 많아, 분리했습니다.
    private fun initVariable() {
        val goHome=binding.gohome
        recyclerView=binding.recyclerView

        val pick=binding.pick
        adapter = RecyclerAdapter(bit)

        recyclerView.layoutManager=LinearLayoutManager(this@ClosetActivity,LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter


        pick.setOnClickListener {
            val myIntent=Intent(this,DetectActivity::class.java)
            startActivity(myIntent)
        }
        goHome.setOnClickListener {
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