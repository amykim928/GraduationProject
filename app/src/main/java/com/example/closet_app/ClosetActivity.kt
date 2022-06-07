package com.example.closet_app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.closet_app.databinding.ActivityClosetBinding
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
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
        val dirs = File("$filesDir/save")
        if (!dirs.exists()){
            dirs.mkdirs()
        }
        val fileDirs=dirs.listFiles()



        if(fileDirs != null){
            if(fileDirs.isEmpty()){
                Log.i("warning","cannot find dir or no files")
            }else{
                for(tmpFile in fileDirs){
                    Log.i("files", tmpFile.name)
                    val path="$dirs/${tmpFile.name}"
                    if(".jpg" in path){
                        bit.add(BitmapFactory.decodeFile(path))
                    }

                }
            }
        }else{
            Log.i("error","filedirs null")
        }
        val inits = File("$filesDir/save/3.jpg")
        if (!inits.exists()){
            inits.createNewFile()
            val assetManager=resources.assets.open("blackskirt.jpg")
            val mkbitmap= Drawable.createFromStream(assetManager,null) as BitmapDrawable
            val finalBit=mkbitmap.bitmap
            finalBit.compress(Bitmap.CompressFormat.JPEG,100, FileOutputStream(inits))
            bit.add(finalBit)
        }
        val init2=File("$filesDir/save/3.txt")
        if (!init2.exists()){
            init2.createNewFile()
            val printWriter= FileWriter(init2)
            val buffer= BufferedWriter(printWriter)
            buffer.write("스커트")
            buffer.close()
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